package org.hygorp.bookmarketplace.repositories;

import org.hygorp.bookmarketplace.entities.AddressEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("AddressRepositoryTests")
public class AddressRepositoryTest {
    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void beforeEach() {
        addressRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        addressRepository.deleteAll();
    }

    @Test
    @DisplayName("should save address")
    @Order(1)
    void shouldSaveAddress() {
        AddressEntity address = Assertions.assertDoesNotThrow(() -> addressRepository.save(new AddressEntity(
                "Avenida das Acacias, 55",
                "Sao Paulo",
                "SP",
                "12452181",
                "Brasil",
                null
        )));

        Assertions.assertNotNull(address);
        Assertions.assertEquals("Avenida das Acacias, 55", address.getAddressLine());
    }

    @Test
    @DisplayName("should find address by id")
    @Order(2)
    void ShouldFindAddressById() {
        AddressEntity address = Assertions.assertDoesNotThrow(() -> addressRepository.save(new AddressEntity(
                "Avenida das Acacias, 55",
                "Sao Paulo",
                "SP",
                "12452181",
                "Brasil",
                null
        )));

        UUID myAddressId = address.getId();

        AddressEntity savedAddress = Assertions.assertDoesNotThrow(() -> addressRepository.findById(myAddressId).orElseThrow());

        Assertions.assertEquals(address.getAddressLine(), savedAddress.getAddressLine());
        Assertions.assertEquals(address.getCity(), savedAddress.getCity());
    }

    @Test
    @DisplayName("should update address")
    @Order(3)
    void shouldUpdateAddress() {
        AddressEntity address = Assertions.assertDoesNotThrow(() -> addressRepository.save(new AddressEntity(
                "Avenida das Acacias, 56",
                "Sao Paulo",
                "SP",
                "12452181",
                "Brasil",
                null
        )));

        UUID myAddressId = address.getId();

        AddressEntity savedAddress = Assertions.assertDoesNotThrow(() -> addressRepository.findById(myAddressId).orElseThrow());
        savedAddress.setAddressLine("Avenida das Acacias, 566");

        AddressEntity updatedAddress = Assertions.assertDoesNotThrow(() -> addressRepository.save(savedAddress));

        Assertions.assertEquals(myAddressId, updatedAddress.getId());
        Assertions.assertEquals("Avenida das Acacias, 566", updatedAddress.getAddressLine());
    }

    @Test
    @DisplayName("should delete Address")
    @Order(4)
    void shouldDeleteAddress() {
        AddressEntity address = Assertions.assertDoesNotThrow(() -> addressRepository.save(new AddressEntity(
                "Avenida das Acacias, 56",
                "Sao Paulo",
                "SP",
                "12452181",
                "Brasil",
                null
        )));

        UUID myAddressId = address.getId();

        addressRepository.deleteById(myAddressId);

        Assertions.assertThrows(NoSuchElementException.class, () -> addressRepository.findById(myAddressId).orElseThrow());
    }
}
