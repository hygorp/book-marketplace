package org.hygorp.bookmarketplace.repositories;

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
@DisplayName("UserRepositoryTests")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("should save user")
    @Order(1)
    void shouldSaveUser() {
        UserEntity user = Assertions.assertDoesNotThrow(() -> userRepository.save(new UserEntity(
                "joseph",
                "123",
                "client"
        )));

        Assertions.assertNotNull(user);
        Assertions.assertEquals("joseph", user.getUsername());
        Assertions.assertEquals("123", user.getPassword());
    }

    @Test
    @DisplayName("should find user by id")
    @Order(2)
    void ShouldFindUserById() {
        UserEntity user = Assertions.assertDoesNotThrow(() -> userRepository.save(new UserEntity(
                "joseph",
                "123",
                "client"
        )));

        UUID myUserId = user.getId();

        UserEntity savedUser = Assertions.assertDoesNotThrow(() -> userRepository.findById(myUserId).orElseThrow());

        Assertions.assertEquals("joseph", savedUser.getUsername());
        Assertions.assertEquals("123", savedUser.getPassword());
        Assertions.assertEquals("client", savedUser.getRole());
    }

    @Test
    @DisplayName("should update user")
    @Order(3)
    void shouldUpdateUser() {
        UserEntity user = Assertions.assertDoesNotThrow(() -> userRepository.save(new UserEntity(
                "joseph",
                "123",
                "client"
        )));

        UUID myUserId = user.getId();

        UserEntity savedUser = Assertions.assertDoesNotThrow(() -> userRepository.findById(myUserId).orElseThrow());
        savedUser.setUsername("joseph1");

        UserEntity updatedClient = Assertions.assertDoesNotThrow(() -> userRepository.save(savedUser));

        Assertions.assertEquals("joseph1", updatedClient.getUsername());
        Assertions.assertEquals("client", updatedClient.getRole());
    }

    @Test
    @DisplayName("should delete user")
    @Order(4)
    void shouldDeleteUser() {
        UserEntity user = Assertions.assertDoesNotThrow(() -> userRepository.save(new UserEntity(
                "joseph",
                "123",
                "client"
        )));

        UUID myUserId = user.getId();

        userRepository.deleteById(myUserId);

        Assertions.assertThrows(NoSuchElementException.class, () -> userRepository.findById(myUserId).orElseThrow());
    }
}
