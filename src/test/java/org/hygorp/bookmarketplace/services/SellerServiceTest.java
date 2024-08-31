package org.hygorp.bookmarketplace.services;

import org.hygorp.bookmarketplace.entities.AddressEntity;
import org.hygorp.bookmarketplace.entities.SellerEntity;
import org.hygorp.bookmarketplace.records.Seller;
import org.hygorp.bookmarketplace.repositories.SellerRepository;
import org.hygorp.bookmarketplace.services.exceptions.SellerServiceException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("SellerServiceTests")
public class SellerServiceTest {
    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private SellerService sellerService;

    private UUID mySellerId01;
    private UUID mySellerId02;

    @BeforeEach
    void beforeEach() {
        sellerRepository.deleteAll();

        mySellerId01 = sellerService.save(
                new SellerEntity(
                        "Seller Test 01",
                        "551132442526",
                        "https://image.com/seller-01-logo.jpg",
                        Instant.now(),
                        new AddressEntity(
                                "Avenida das Acacias, 55",
                                "Sao Paulo",
                                "SP",
                                "12452181",
                                "Brasil",
                                null
                        )
                )
        ).getId();

        mySellerId02 = sellerRepository.save(
                new SellerEntity(
                        "Seller Test 02",
                        "554145782521",
                        "https://image.com/seller-02-logo.jpg",
                        Instant.now(),
                        new AddressEntity(
                                "Avenida das Araucarias, 226",
                                "Curitiba",
                                "PR",
                                "78998745",
                                "Brasil",
                                null
                        )
                )
        ).getId();
    }

    @AfterEach
    void afterEach() {
        sellerRepository.deleteAll();
    }

    @Test
    @DisplayName("should return all sellers")
    @Order(1)
    void shouldReturnAllSellers() {
        Page<Seller> sellers = Assertions.assertDoesNotThrow(() -> sellerService.findAll(Pageable.ofSize(10)));

        Assertions.assertNotNull(sellers);
        Assertions.assertEquals(2, sellers.getTotalElements());
    }

    @Test
    @DisplayName("should return seller by id and cascade address")
    @Order(2)
    void shouldReturnSellerByIdAndCascadeAddress() {
        SellerEntity seller = Assertions.assertDoesNotThrow(() -> sellerService.findById(mySellerId01));

        Assertions.assertNotNull(seller);
        Assertions.assertEquals(mySellerId01, seller.getId());
        Assertions.assertEquals("Seller Test 01", seller.getName());
        Assertions.assertEquals("Avenida das Acacias, 55", seller.getAddress().getAddressLine());
    }

    @Test
    @DisplayName("shouldn't return seller by id and throws exception")
    @Order(3)
    void shouldNotReturnSellerByIdAndThrowException() {
        Assertions.assertThrows(SellerServiceException.class, () -> sellerService.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("should return sellers by name")
    @Order(4)
    void shouldReturnSellersByName() {
        Set<SellerEntity> sellers = Assertions.assertDoesNotThrow(() -> sellerService.findByName("Seller Test"));

        Assertions.assertNotNull(sellers);
        Assertions.assertEquals(2, sellers.size());
    }

    @Test
    @DisplayName("should save seller and address in cascade")
    @Order(5)
    void shouldSaveSellerAndAddressInCascade() {
        SellerEntity seller = Assertions.assertDoesNotThrow(() -> sellerService.save(new SellerEntity(
                "Seller Test 03",
                "558733566121",
                "https://image.com/seller-03-logo.jpg",
                Instant.now(),
                new AddressEntity(
                        "Avenida das Uvas, 900",
                        "Petrolina",
                        "PE",
                        "59100100",
                        "Brasil",
                        null
                )
        )));

        Assertions.assertNotNull(seller);
        Assertions.assertEquals("Seller Test 03", seller.getName());
        Assertions.assertEquals("Avenida das Uvas, 900", seller.getAddress().getAddressLine());
    }

    @Test
    @DisplayName("should update seller and address in cascade")
    @Order(6)
    void shouldUpdateSellerAndAddressInCascade() {
        SellerEntity seller = Assertions.assertDoesNotThrow(() -> sellerService.findById(mySellerId02));
        Assertions.assertNotNull(seller);

        seller.setName("Seller Test 02 - (Edited)");
        seller.getAddress().setAddressLine("Avenida das Araucarias, 1000");

        SellerEntity updatedSeller = Assertions.assertDoesNotThrow(() -> sellerService.update(mySellerId02, seller));

        Assertions.assertNotNull(updatedSeller);
        Assertions.assertEquals("Seller Test 02 - (Edited)", updatedSeller.getName());
        Assertions.assertEquals("Avenida das Araucarias, 1000", updatedSeller.getAddress().getAddressLine());
    }

    @Test
    @DisplayName("shouldn't update seller and throws exception")
    @Order(7)
    void shouldNotUpdateSellerAndThrowException() {
        Assertions.assertThrows(SellerServiceException.class, () -> sellerService.update(UUID.randomUUID(), new SellerEntity()));
    }

    @Test
    @DisplayName("should delete seller")
    @Order(8)
    void shouldDeleteSeller() {
        Assertions.assertDoesNotThrow(() -> sellerService.delete(mySellerId01));

        Assertions.assertThrows(SellerServiceException.class, () -> sellerService.findById(mySellerId01));
    }
}
