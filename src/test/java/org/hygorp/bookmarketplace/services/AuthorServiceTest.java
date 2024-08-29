package org.hygorp.bookmarketplace.services;

import org.hygorp.bookmarketplace.entities.AuthorEntity;
import org.hygorp.bookmarketplace.repositories.AuthorRepository;
import org.hygorp.bookmarketplace.services.exceptions.AuthorServiceException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("AuthorServiceTests")
public class AuthorServiceTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    private UUID myAuthorId01;
    private UUID myAuthorId02;

    @BeforeEach
    void beforeEach() {
        authorRepository.deleteAll();

        AuthorEntity orwell = authorRepository.save(
                new AuthorEntity(
                        "George Orwell",
                        "Eric Arthur Blair, better known by the pseudonym George Orwell, was an English writer, journalist and political essayist, born in British India",
                        "https://image.com/george_orwell.jpg"
                )
        );

        AuthorEntity rowling = authorRepository.save(
                new AuthorEntity(
                        "J.K Rowling",
                        "Joanne 'Jo' Rowling, better known as J. K. Rowling, is a British writer, screenwriter and film producer, known for writing the Harry Potter book series.",
                        "https://image.com/jk_rowling.jpg"
                )
        );

        myAuthorId01 = authorRepository.save(orwell).getId();
        myAuthorId02 = authorRepository.save(rowling).getId();
    }

    @AfterEach
    void afterEach() {
        authorRepository.deleteAll();
    }

    @Test
    @DisplayName("should return all authors")
    @Order(1)
    void shouldReturnAllAuthors() {
        Page<AuthorEntity> authors = Assertions.assertDoesNotThrow(() -> authorService.findAll(Pageable.ofSize(10)));

        Assertions.assertNotNull(authors);
        Assertions.assertEquals(2, authors.getTotalElements());
    }

    @Test
    @DisplayName("should return author by id")
    @Order(2)
    void shouldReturnAuthorById() {
        AuthorEntity author = Assertions.assertDoesNotThrow(() -> authorService.findById(myAuthorId01));

        Assertions.assertNotNull(author);
        Assertions.assertEquals(myAuthorId01, author.getId());
        Assertions.assertEquals("George Orwell", author.getName());
    }

    @Test
    @DisplayName("shouldn't return author by id and throws exception")
    @Order(3)
    void shouldNotReturnAuthorByIdAndThrowException() {
        Assertions.assertThrows(AuthorServiceException.class, () -> authorService.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("should save author")
    @Order(4)
    void shouldSaveAuthor() {
        AuthorEntity author = Assertions.assertDoesNotThrow(() -> authorService.save(new AuthorEntity(
                "Test Author 03",
                "Test Bio 03",
                "https://image.com/test.jpg"
        )));

        Assertions.assertNotNull(author);
        Assertions.assertEquals("Test Author 03", author.getName());
    }

    @Test
    @DisplayName("should update author")
    @Order(5)
    void shouldUpdateAuthor() {
        AuthorEntity author = Assertions.assertDoesNotThrow(() -> authorService.findById(myAuthorId02));
        Assertions.assertNotNull(author);

        author.setName("J.K Rowling - (Edited)");

        AuthorEntity updatedAuthor = Assertions.assertDoesNotThrow(() -> authorService.update(myAuthorId02, author));

        Assertions.assertNotNull(updatedAuthor);
        Assertions.assertEquals("J.K Rowling - (Edited)", updatedAuthor.getName());
    }

    @Test
    @DisplayName("shouldn't update author and throws exception")
    @Order(6)
    void shouldNotUpdateAuthorAndThrowException() {
        Assertions.assertThrows(AuthorServiceException.class, () -> authorService.update(UUID.randomUUID(), new AuthorEntity()));
    }

    @Test
    @DisplayName("should delete author")
    @Order(7)
    void shouldDeleteAuthor() {
        Assertions.assertDoesNotThrow(() -> authorService.delete(myAuthorId01));

        Assertions.assertThrows(AuthorServiceException.class, () -> authorService.findById(myAuthorId01));
    }
}
