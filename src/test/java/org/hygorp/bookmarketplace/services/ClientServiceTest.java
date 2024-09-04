package org.hygorp.bookmarketplace.services;

import org.hygorp.bookmarketplace.entities.AddressEntity;
import org.hygorp.bookmarketplace.entities.ClientEntity;
import org.hygorp.bookmarketplace.entities.UserEntity;
import org.hygorp.bookmarketplace.repositories.ClientRepository;
import org.hygorp.bookmarketplace.services.exceptions.ClientServiceException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("ClientServiceTests")
public class ClientServiceTest {
    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    UUID myClientId01;
    UUID myClientId02;

    @BeforeEach
    void beforeEach() {
        clientRepository.deleteAll();

        myClientId01 = clientRepository.save(new ClientEntity(
                "Joseph First",
                "12345678910",
                "joseph@mail.com",
                "559854541258",
                new UserEntity(
                        "joseph",
                        "123",
                        "client"
                )
        )).getId();

        myClientId02 = clientRepository.save(new ClientEntity(
                "Hannah First",
                "456123987401",
                "hannah@mail.com",
                "1288454879865",
                new UserEntity(
                        "hannah",
                        "123",
                        "user"
                )
        )).getId();
    }

    @AfterEach
    void afterEach() {
        clientRepository.deleteAll();
    }

    @Test
    @DisplayName("should find client by id")
    @Order(1)
    void shouldFindClientById() {
        ClientEntity client = Assertions.assertDoesNotThrow(() -> clientService.findById(myClientId01));

        Assertions.assertNotNull(client);
        Assertions.assertEquals(myClientId01, client.getId());
        Assertions.assertEquals("Joseph First", client.getName());
        Assertions.assertEquals("joseph", client.getCredentials().getUsername());
    }

    @Test
    @DisplayName("shouldn't find client by id")
    @Order(2)
    void shouldNotFindClientById() {
        Assertions.assertThrows(ClientServiceException.class, () -> clientService.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("should find client by username")
    @Order(3)
    void shouldFindClientByUsername() {
        ClientEntity client = Assertions.assertDoesNotThrow(() -> clientService.findByUsername("joseph"));

        Assertions.assertNotNull(client);
        Assertions.assertEquals("Joseph First", client.getName());
        Assertions.assertEquals("client", client.getCredentials().getRole());
    }

    @Test
    @DisplayName("shouldn't fin client by username")
    @Order(4)
    void shouldNotFindClientByUsername() {
        Assertions.assertThrows(ClientServiceException.class, () -> clientService.findByUsername("myuser"));
    }

    @Test
    @DisplayName("should save client, address and user in cascade")
    @Order(5)
    void shouldSaveClientAddressAndUserInCascade() {
        ClientEntity client = new ClientEntity(
                "Abraham Lincoln",
                "9517532846",
                "abraham@mail.com",
                "1185745789865",
                new UserEntity(
                        "abrahaml",
                        "123456",
                        "admin"
                )
        );

        client.getAddresses().add(new AddressEntity(
                "413 S 8th St",
                "Springfield",
                "Illinois",
                "62701",
                "EUA",
                ""
        ));

        ClientEntity savedClient = Assertions.assertDoesNotThrow(() -> clientService.save(client));

        Assertions.assertNotNull(savedClient);
        Assertions.assertEquals("Abraham Lincoln", savedClient.getName());
        Assertions.assertEquals("abrahaml", savedClient.getCredentials().getUsername());
        Assertions.assertEquals(1, savedClient.getAddresses().size());
    }

    @Test
    @DisplayName("should update client and user in cascade")
    @Order(6)
    void shouldUpdateClientAndUserInCascade() {
        ClientEntity client = new ClientEntity(
                "Abraham Lincoln",
                "9517532846",
                "abraham@mail.com",
                "1185745789865",
                new UserEntity(
                        "abrahaml",
                        "123456",
                        "admin"
                )
        );

        client.getAddresses().add(new AddressEntity(
                "413 S 8th St",
                "Springfield",
                "Illinois",
                "62701",
                "EUA",
                ""
        ));

        UUID myClientId = Assertions.assertDoesNotThrow(() -> clientService.save(client)).getId();

        ClientEntity savedClient = Assertions.assertDoesNotThrow(() -> clientService.findById(myClientId));

        savedClient.setEmail("abrahaml@mail.com.us");
        savedClient.getCredentials().setUsername("abraham_l");

        ClientEntity updatedClient = Assertions.assertDoesNotThrow(() ->clientService.update(myClientId, savedClient));

        Assertions.assertNotNull(updatedClient);
        Assertions.assertEquals("Abraham Lincoln", updatedClient.getName());
        Assertions.assertEquals("abrahaml@mail.com.us", updatedClient.getEmail());
        Assertions.assertEquals("abraham_l", updatedClient.getCredentials().getUsername());
        Assertions.assertEquals("123456", updatedClient.getCredentials().getPassword());
        Assertions.assertEquals(1, updatedClient.getAddresses().size());
    }

    @Test
    @DisplayName("shouldn't update client")
    @Order(7)
    void shouldNotUpdateClient() {
        Assertions.assertThrows(ClientServiceException.class, () -> clientService.update(UUID.randomUUID(), new ClientEntity()));
    }

    @Test
    @DisplayName("should delete client, addresses and user in cascade")
    @Order(8)
    void shouldDeleteClientAddressAndUserInCascade() {
        ClientEntity client = new ClientEntity(
                "Abraham Lincoln",
                "9517532846",
                "abraham@mail.com",
                "1185745789865",
                new UserEntity(
                        "abrahaml",
                        "123456",
                        "admin"
                )
        );

        client.getAddresses().add(new AddressEntity(
                "413 S 8th St",
                "Springfield",
                "Illinois",
                "62701",
                "EUA",
                ""
        ));

        UUID myClientId = Assertions.assertDoesNotThrow(() -> clientService.save(client)).getId();

        clientService.delete(myClientId);

        Assertions.assertThrows(ClientServiceException.class, () -> clientService.findById(myClientId));
    }
}
