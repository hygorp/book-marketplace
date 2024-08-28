package org.hygorp.bookmarketplace.repositories;

import org.hygorp.bookmarketplace.entities.BookEntity;
import org.hygorp.bookmarketplace.enums.Condition;
import org.hygorp.bookmarketplace.enums.CoverType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("BookRepositoryTests")
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("should save book")
    @Order(1)
    void shouldSaveBook() {
        BookEntity book = Assertions.assertDoesNotThrow(() -> bookRepository.save(new BookEntity(
                "Book Test 01",
                "Description Test 01",
                LocalDate.parse("1970-01-01"),
                "1012242628",
                "https://image.com/book-test-01.jpg",
                10,
                Condition.NEW,
                CoverType.HARDCOVER
        )));

        Assertions.assertNotNull(book);
        Assertions.assertEquals("Book Test 01", book.getTitle());
    }

    @Test
    @DisplayName("should find book by id")
    @Order(2)
    void ShouldFindBookById() {
        BookEntity book = Assertions.assertDoesNotThrow(() -> bookRepository.save(new BookEntity(
                "Book Test 01",
                "Description Test 01",
                LocalDate.parse("1970-01-01"),
                "1012242628",
                "https://image.com/book-test-01.jpg",
                10,
                Condition.NEW,
                CoverType.HARDCOVER
        )));

        UUID myBookId = book.getId();

        BookEntity savedBook = Assertions.assertDoesNotThrow(() -> bookRepository.findById(myBookId).orElseThrow());

        Assertions.assertEquals(book.getTitle(), savedBook.getTitle());
        Assertions.assertEquals("1012242628", savedBook.getIsbn());
    }

    @Test
    @DisplayName("should update book")
    @Order(3)
    void shouldUpdateBook() {
        BookEntity book = Assertions.assertDoesNotThrow(() -> bookRepository.save(new BookEntity(
                "Book Test 01",
                "Description Test 01",
                LocalDate.parse("1970-01-01"),
                "1012242628",
                "https://image.com/book-test-01.jpg",
                10,
                Condition.NEW,
                CoverType.HARDCOVER
        )));

        UUID myBookId = book.getId();

        BookEntity savedBook = Assertions.assertDoesNotThrow(() -> bookRepository.findById(myBookId).orElseThrow());
        savedBook.setTitle("Book Test 01 (edited)");
        savedBook.setIsbn("999");

        BookEntity editedBook = Assertions.assertDoesNotThrow(() -> bookRepository.save(savedBook));

        Assertions.assertEquals(myBookId, editedBook.getId());
        Assertions.assertEquals("Book Test 01 (edited)", editedBook.getTitle());
        Assertions.assertEquals("999", editedBook.getIsbn());
    }

    @Test
    @DisplayName("should delete book")
    @Order(4)
    void shouldDeleteBook() {
        BookEntity book = Assertions.assertDoesNotThrow(() -> bookRepository.save(new BookEntity(
                "Book Test 01",
                "Description Test 01",
                LocalDate.parse("1970-01-01"),
                "1012242628",
                "https://image.com/book-test-01.jpg",
                10,
                Condition.NEW,
                CoverType.HARDCOVER
        )));

        UUID myBookId = book.getId();

        bookRepository.deleteById(myBookId);

        Assertions.assertThrows(NoSuchElementException.class, () -> bookRepository.findById(myBookId).orElseThrow());
    }
}
