package org.hygorp.bookmarketplace.services;

import org.hygorp.bookmarketplace.entities.PublisherEntity;
import org.hygorp.bookmarketplace.repositories.PublisherRepository;
import org.hygorp.bookmarketplace.services.exceptions.PublisherServiceException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("PublisherServiceTests")
public class PublisherServiceTest {
    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private PublisherService publisherService;

    private UUID myPublisherId01;
    private UUID myPublisherId02;

    @BeforeEach
    void beforeEach() {
        publisherRepository.deleteAll();

        PublisherEntity publisher01 = publisherRepository.save(
                new PublisherEntity("Publisher Test 01", "https://image.com/publisher-test-01-logo.jpg")
        );

        PublisherEntity publisher02 = publisherRepository.save(
                new PublisherEntity("Publisher Test 02", "https://image.com/publisher-test-02-logo.jpg")
        );

        myPublisherId01 = publisherRepository.save(publisher01).getId();
        myPublisherId02 = publisherRepository.save(publisher02).getId();
    }

    @AfterEach
    void afterEach() {
        publisherRepository.deleteAll();
    }

    @Test
    @DisplayName("should return all publishers")
    @Order(1)
    void shouldReturnAllPublishers() {
        Page<PublisherEntity> publishers = Assertions.assertDoesNotThrow(() -> publisherService.findAll(Pageable.ofSize(10)));

        Assertions.assertNotNull(publishers);
        Assertions.assertEquals(2, publishers.getTotalElements());
    }

    @Test
    @DisplayName("should return publisher by id")
    @Order(2)
    void shouldReturnPublisherById() {
        PublisherEntity publisher = Assertions.assertDoesNotThrow(() -> publisherService.findById(myPublisherId01));

        Assertions.assertNotNull(publisher);
        Assertions.assertEquals(myPublisherId01, publisher.getId());
        Assertions.assertEquals("Publisher Test 01", publisher.getName());
    }

    @Test
    @DisplayName("shouldn't return publisher by id and throws exception")
    @Order(3)
    void shouldNotReturnPublisherByIdAndThrowException() {
        Assertions.assertThrows(PublisherServiceException.class, () -> publisherService.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("should return publishers by name")
    @Order(4)
    void shouldReturnPublishersByName() {
        Set<PublisherEntity> publishers = Assertions.assertDoesNotThrow(() -> publisherService.findByName("Publisher Test"));

        Assertions.assertNotNull(publishers);
        Assertions.assertEquals(2, publishers.size());
    }

    @Test
    @DisplayName("should save publisher")
    @Order(5)
    void shouldSavePublisher() {
        PublisherEntity publisher = Assertions.assertDoesNotThrow(() -> publisherService.save(new PublisherEntity(
                "Publisher Test 03",
                "https://image.com/publisher-03-test.jpg"
        )));

        Assertions.assertNotNull(publisher);
        Assertions.assertEquals("Publisher Test 03", publisher.getName());
    }

    @Test
    @DisplayName("should update publisher")
    @Order(6)
    void shouldUpdatePublisher() {
        PublisherEntity publisher = Assertions.assertDoesNotThrow(() -> publisherService.findById(myPublisherId02));
        Assertions.assertNotNull(publisher);

        publisher.setName("Publisher Test 02 - (Edited)");

        PublisherEntity updatedPublisher = Assertions.assertDoesNotThrow(() -> publisherService.update(myPublisherId02, publisher));

        Assertions.assertNotNull(updatedPublisher);
        Assertions.assertEquals("Publisher Test 02 - (Edited)", updatedPublisher.getName());
    }

    @Test
    @DisplayName("shouldn't update publisher and throws exception")
    @Order(7)
    void shouldNotUpdatePublisherAndThrowException() {
        Assertions.assertThrows(PublisherServiceException.class, () -> publisherService.update(UUID.randomUUID(), new PublisherEntity()));
    }

    @Test
    @DisplayName("should delete publisher")
    @Order(8)
    void shouldDeletePublisher() {
        Assertions.assertDoesNotThrow(() -> publisherService.delete(myPublisherId01));

        Assertions.assertThrows(PublisherServiceException.class, () -> publisherService.findById(myPublisherId01));
    }
}
