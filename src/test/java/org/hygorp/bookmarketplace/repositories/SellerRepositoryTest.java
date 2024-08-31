package org.hygorp.bookmarketplace.repositories;

import org.hygorp.bookmarketplace.entities.AddressEntity;
import org.hygorp.bookmarketplace.entities.SellerEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("SellerRepositoryTests")
public class SellerRepositoryTest {
    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void beforeEach() {
        sellerRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        sellerRepository.deleteAll();
    }

    @Test
    @DisplayName("should save seller and address in cascade")
    @Order(1)
    void shouldSaveSellerAndAddressInCascade() {
        SellerEntity seller = Assertions.assertDoesNotThrow(() -> sellerRepository.save(new SellerEntity(
                "Seller Test 01",
                "551132442526",
                "https://image.com/publisher-01-logo.jpg",
                Instant.now(),
                new AddressEntity(
                        "Avenida das Acacias, 55",
                        "Sao Paulo",
                        "SP",
                        "12452181",
                        "Brasil",
                        null
                )
        )));

        Assertions.assertNotNull(seller);
        Assertions.assertEquals("Seller Test 01", seller.getName());
        Assertions.assertEquals("Avenida das Acacias, 55", seller.getAddress().getAddressLine());
    }

    @Test
    @DisplayName("should find seller by id")
    @Order(2)
    void ShouldFindSellerById() {
        SellerEntity seller = Assertions.assertDoesNotThrow(() -> sellerRepository.save(new SellerEntity(
                "Seller Test 01",
                "551132442526",
                "https://image.com/publisher-01-logo.jpg",
                Instant.now(),
                new AddressEntity(
                        "Avenida das Acacias, 55",
                        "Sao Paulo",
                        "SP",
                        "12452181",
                        "Brasil",
                        null
                )
        )));

        UUID mySellerId = seller.getId();

        SellerEntity savedSeller = Assertions.assertDoesNotThrow(() -> sellerRepository.findById(mySellerId).orElseThrow());

        Assertions.assertEquals(seller.getName(), savedSeller.getName());
        Assertions.assertEquals(seller.getAddress().getCity(), savedSeller.getAddress().getCity());
    }

    @Test
    @DisplayName("should update seller and address in cascade")
    @Order(3)
    void shouldUpdateSellerAndAddressInCascade() {
        SellerEntity seller = Assertions.assertDoesNotThrow(() -> sellerRepository.save(new SellerEntity(
                "Seller Test 01",
                "551132442526",
                "https://image.com/publisher-01-logo.jpg",
                Instant.now(),
                new AddressEntity(
                        "Avenida das Acacias, 56",
                        "Sao Paulo",
                        "SP",
                        "12452181",
                        "Brasil",
                        null
                )
        )));

        UUID mySellerId = seller.getId();

        SellerEntity savedSeller = Assertions.assertDoesNotThrow(() -> sellerRepository.findById(mySellerId).orElseThrow());
        savedSeller.setName("Seller Test 01 (edited)");
        savedSeller.getAddress().setAddressLine("Avenida das Acacias, 156");

        SellerEntity updatedSeller = Assertions.assertDoesNotThrow(() -> sellerRepository.save(savedSeller));

        Assertions.assertEquals(mySellerId, updatedSeller.getId());
        Assertions.assertEquals("Seller Test 01 (edited)", updatedSeller.getName());
        Assertions.assertEquals("Avenida das Acacias, 156", updatedSeller.getAddress().getAddressLine());
    }

    @Test
    @DisplayName("should delete seller and address in cascade")
    @Order(4)
    void shouldDeleteSellerAndAddressInCascade() {
        SellerEntity seller = Assertions.assertDoesNotThrow(() -> sellerRepository.save(new SellerEntity(
                "Seller Test 01",
                "551132442526",
                "https://image.com/publisher-01-logo.jpg",
                Instant.now(),
                new AddressEntity(
                        "Avenida das Acacias, 56",
                        "Sao Paulo",
                        "SP",
                        "12452181",
                        "Brasil",
                        null
                )
        )));

        UUID mySellerId = seller.getId();
        UUID sellerAddressId = seller.getAddress().getId();

        sellerRepository.deleteById(mySellerId);

        Assertions.assertThrows(NoSuchElementException.class, () -> sellerRepository.findById(mySellerId).orElseThrow());
        Assertions.assertThrows(NoSuchElementException.class, () -> addressRepository.findById(sellerAddressId).orElseThrow());
    }
}
