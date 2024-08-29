package org.hygorp.bookmarketplace.repositories;

import org.hygorp.bookmarketplace.entities.AuthorEntity;
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
@DisplayName("AuthorRepositoryTests")
public class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void beforeEach() {
        authorRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        authorRepository.deleteAll();
    }

    @Test
    @DisplayName("should save author")
    @Order(1)
    void shouldSaveAuthor() {
        AuthorEntity author = Assertions.assertDoesNotThrow(() -> authorRepository.save(new AuthorEntity(
                "Author Test 01",
                "Bio Test 01",
                "https://image.com/author-test-01.jpg"
        )));

        Assertions.assertNotNull(author);
        Assertions.assertEquals("Author Test 01", author.getName());
    }

    @Test
    @DisplayName("should find author by id")
    @Order(2)
    void ShouldFindAuthorById() {
        AuthorEntity author = Assertions.assertDoesNotThrow(() -> authorRepository.save(new AuthorEntity(
                "Author Test 01",
                "Bio Test 01",
                "https://image.com/author-test-01.jpg"
        )));

        UUID myAuthorId = author.getId();

        AuthorEntity savedAuthor = Assertions.assertDoesNotThrow(() -> authorRepository.findById(myAuthorId).orElseThrow());

        Assertions.assertEquals(author.getName(), savedAuthor.getName());
    }

    @Test
    @DisplayName("should update author")
    @Order(3)
    void shouldUpdateAuthor() {
        AuthorEntity author = Assertions.assertDoesNotThrow(() -> authorRepository.save(new AuthorEntity(
                "Author Test 01",
                "Bio Test 01",
                "https://image.com/author-test-01.jpg"
        )));

        UUID myAuthorId = author.getId();

        AuthorEntity savedAuthor = Assertions.assertDoesNotThrow(() -> authorRepository.findById(myAuthorId).orElseThrow());
        savedAuthor.setName("Author Test 01 (edited)");

        AuthorEntity editedAuthor = Assertions.assertDoesNotThrow(() -> authorRepository.save(savedAuthor));

        Assertions.assertEquals(myAuthorId, editedAuthor.getId());
        Assertions.assertEquals("Author Test 01 (edited)", editedAuthor.getName());
    }

    @Test
    @DisplayName("should delete author")
    @Order(4)
    void shouldDeleteAuthor() {
        AuthorEntity author = Assertions.assertDoesNotThrow(() -> authorRepository.save(new AuthorEntity(
                "Author Test 01",
                "Bio Test 01",
                "https://image.com/author-test-01.jpg"
        )));

        UUID myAuthorId = author.getId();

        authorRepository.deleteById(myAuthorId);

        Assertions.assertThrows(NoSuchElementException.class, () -> authorRepository.findById(myAuthorId).orElseThrow());
    }
}
