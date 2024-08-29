package org.hygorp.bookmarketplace.repositories;

import org.hygorp.bookmarketplace.entities.PublisherEntity;
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
@DisplayName("PublisherRepositoryTests")
public class PublisherRepositoryTest {
    @Autowired
    private PublisherRepository publisherRepository;

    @BeforeEach
    void beforeEach() {
        publisherRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        publisherRepository.deleteAll();
    }

    @Test
    @DisplayName("should save publisher")
    @Order(1)
    void shouldSavePublisher() {
        PublisherEntity publisher = Assertions.assertDoesNotThrow(() -> publisherRepository.save(new PublisherEntity(
                "Publisher Test 01",
                "https://image.com/publisher-01-logo.jpg"
        )));

        Assertions.assertNotNull(publisher);
        Assertions.assertEquals("Publisher Test 01", publisher.getName());
    }

    @Test
    @DisplayName("should find publisher by id")
    @Order(2)
    void ShouldFindPublisherById() {
        PublisherEntity publisher = Assertions.assertDoesNotThrow(() -> publisherRepository.save(new PublisherEntity(
                "Publisher Test 01",
                "https://image.com/publisher-01-logo.jpg"
        )));

        UUID myPublisherId = publisher.getId();

        PublisherEntity savedPublisher = Assertions.assertDoesNotThrow(() -> publisherRepository.findById(myPublisherId).orElseThrow());

        Assertions.assertEquals(publisher.getName(), savedPublisher.getName());
    }

    @Test
    @DisplayName("should update publisher")
    @Order(3)
    void shouldUpdatePublisher() {
        PublisherEntity publisher = Assertions.assertDoesNotThrow(() -> publisherRepository.save(new PublisherEntity(
                "Publisher Test 01",
                "https://image.com/publisher-01-logo.jpg"
        )));

        UUID myPublisherId = publisher.getId();

        PublisherEntity savedPublisher = Assertions.assertDoesNotThrow(() -> publisherRepository.findById(myPublisherId).orElseThrow());
        savedPublisher.setName("Publisher Test 01 (edited)");

        PublisherEntity updatedPublisher = Assertions.assertDoesNotThrow(() -> publisherRepository.save(savedPublisher));

        Assertions.assertEquals(myPublisherId, updatedPublisher.getId());
        Assertions.assertEquals("Publisher Test 01 (edited)", updatedPublisher.getName());
    }

    @Test
    @DisplayName("should delete publisher")
    @Order(4)
    void shouldDeletePublisher() {
        PublisherEntity publisher = Assertions.assertDoesNotThrow(() -> publisherRepository.save(new PublisherEntity(
                "Publisher Test 01",
                "https://image.com/publisher-01-logo.jpg"
        )));

        UUID myPublisherId = publisher.getId();

        publisherRepository.deleteById(myPublisherId);

        Assertions.assertThrows(NoSuchElementException.class, () -> publisherRepository.findById(myPublisherId).orElseThrow());
    }
}
