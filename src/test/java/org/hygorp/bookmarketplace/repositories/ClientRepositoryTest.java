package org.hygorp.bookmarketplace.repositories;

import org.hygorp.bookmarketplace.entities.AddressEntity;
import org.hygorp.bookmarketplace.entities.CartEntity;
import org.hygorp.bookmarketplace.entities.ClientEntity;
import org.hygorp.bookmarketplace.entities.UserEntity;
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
@DisplayName("ClientRepositoryTests")
public class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    void beforeEach() {
        clientRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        clientRepository.deleteAll();
    }

    @Test
    @DisplayName("should save client, user, cart and address in cascade")
    @Order(1)
    void shouldSaveClientUserCartAndAddressInCascade() {
        ClientEntity client = new ClientEntity(
                "Joseph First",
                "joseph@mail.com",
                "559854541258",
                new UserEntity(
                        "joseph",
                        "123",
                        "client"
                )
        );

        client.getAddresses().add(new AddressEntity(
                "Avenida Liberdade, 456",
                "São Paulo",
                "SP",
                "784544100",
                "Brasil",
                null
        ));

        client.getAddresses().add(new AddressEntity(
                "Avenida da Sorte, 567",
                "Vitoria",
                "ES",
                "987485100",
                "Brasil",
                null
        ));

        client.setCart(new CartEntity());

        ClientEntity newClient = Assertions.assertDoesNotThrow(() -> clientRepository.save(client));

        Assertions.assertNotNull(newClient);
        Assertions.assertEquals("Joseph First", newClient.getName());
        Assertions.assertEquals(0, newClient.getCart().getItems().size());
        Assertions.assertEquals("joseph", newClient.getCredentials().getUsername());
        Assertions.assertEquals(2, newClient.getAddresses().size());
    }

    @Test
    @DisplayName("should find client by id")
    @Order(2)
    void ShouldFindClientById() {
        ClientEntity client = Assertions.assertDoesNotThrow(() -> clientRepository.save(new ClientEntity(
                "Joseph First",
                "joseph@mail.com",
                "559854541258",
                new UserEntity(
                        "joseph",
                        "123",
                        "client"
                )
        )));

        UUID myClientId = client.getId();

        ClientEntity savedClient = Assertions.assertDoesNotThrow(() -> clientRepository.findById(myClientId).orElseThrow());

        Assertions.assertEquals(client.getName(), savedClient.getName());
        Assertions.assertEquals("joseph", savedClient.getCredentials().getUsername());
    }

    @Test
    @DisplayName("should update client and user in cascade")
    @org.springframework.core.annotation.Order(3)
    void shouldUpdateClientAndUserInCascade() {
        ClientEntity client = Assertions.assertDoesNotThrow(() -> clientRepository.save(new ClientEntity(
                "Joseph First",
                "joseph@mail.com",
                "559854541258",
                new UserEntity(
                        "joseph",
                        "123",
                        "client"
                )
        )));

        UUID myClientId = client.getId();

        ClientEntity savedClient = Assertions.assertDoesNotThrow(() -> clientRepository.findById(myClientId).orElseThrow());
        savedClient.setName("Joseph First First");
        savedClient.getCredentials().setUsername("joseph11");

        ClientEntity updatedClient = Assertions.assertDoesNotThrow(() -> clientRepository.save(savedClient));

        Assertions.assertEquals(myClientId, updatedClient.getId());
        Assertions.assertEquals("Joseph First First", updatedClient.getName());
        Assertions.assertEquals("joseph11", updatedClient.getCredentials().getUsername());
    }

    @Test
    @DisplayName("should delete client, user, cart and addresses in cascade")
    @Order(4)
    void shouldDeleteClientUserAndAddressesInCascade() {
        ClientEntity client = new ClientEntity(
                "Joseph First",
                "joseph@mail.com",
                "559854541258",
                new UserEntity(
                        "joseph",
                        "123",
                        "client"
                )
        );

        client.getAddresses().add(new AddressEntity(
                "Avenida Liberdade, 456",
                "São Paulo",
                "SP",
                "784544100",
                "Brasil",
                null
        ));

        client.getAddresses().add(new AddressEntity(
                "Avenida da Sorte, 567",
                "Vitoria",
                "ES",
                "987485100",
                "Brasil",
                null
        ));

        client.setCart(new CartEntity());

        ClientEntity newClient = Assertions.assertDoesNotThrow(() -> clientRepository.save(client));

        UUID myClientId = newClient.getId();
        UUID myUserId = newClient.getCredentials().getId();

        clientRepository.deleteById(myClientId);

        Assertions.assertThrows(NoSuchElementException.class, () -> clientRepository.findById(myClientId).orElseThrow());
        Assertions.assertThrows(NoSuchElementException.class, () -> userRepository.findById(myUserId).orElseThrow());
        Assertions.assertEquals(0, addressRepository.findAll().size());
        Assertions.assertEquals(0, cartRepository.findAll().size());
    }
}
