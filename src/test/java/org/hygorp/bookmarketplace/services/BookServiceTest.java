package org.hygorp.bookmarketplace.services;

import org.hygorp.bookmarketplace.entities.AuthorEntity;
import org.hygorp.bookmarketplace.entities.BookEntity;
import org.hygorp.bookmarketplace.entities.GenreEntity;
import org.hygorp.bookmarketplace.enums.Condition;
import org.hygorp.bookmarketplace.enums.CoverType;
import org.hygorp.bookmarketplace.repositories.AuthorRepository;
import org.hygorp.bookmarketplace.repositories.BookRepository;
import org.hygorp.bookmarketplace.repositories.GenreRepository;
import org.hygorp.bookmarketplace.services.exceptions.BookServiceException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("BookServiceTests")
public class BookServiceTest {
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    private UUID myBookId01;
    private UUID myBookId02;

    @BeforeEach
    void beforeEach() {
        bookRepository.deleteAll();

        GenreEntity fiction = genreRepository.save(
                new GenreEntity("Fiction", "https://image.com/fiction.jpg")
        );

        GenreEntity romance = genreRepository.save(
                new GenreEntity("Romance", "https://image.com/romance.jpg")
        );

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

        BookEntity book01 = new BookEntity(
                "Book Test 01",
                "Description Test 01",
                LocalDate.parse("1970-01-01"),
                "123456",
                "https://image.com/book-test-01.jpg",
                10,
                Condition.NEW,
                CoverType.HARDCOVER
        );
        book01.getAuthors().add(orwell);
        book01.getGenres().add(fiction);

        BookEntity book02 = new BookEntity(
                "Book Test 02",
                "Description Test 02",
                LocalDate.parse("1970-01-01"),
                "654321",
                "https://image.com/book-test-02.jpg",
                10,
                Condition.USED,
                CoverType.SOFTCOVER
        );
        book02.getAuthors().add(rowling);
        book02.getGenres().add(romance);

        myBookId01 = bookRepository.save(book01).getId();
        myBookId02 = bookRepository.save(book02).getId();
    }

    @AfterEach
    void afterEach() {
        bookRepository.deleteAll();
    }

    @Test
    @DisplayName("should return all books")
    @Order(1)
    void shouldReturnAllBooks() {
        Page<BookEntity> books = Assertions.assertDoesNotThrow(() -> bookService.findAll(Pageable.ofSize(10)));

        Assertions.assertNotNull(books);
        Assertions.assertEquals(2, books.getTotalElements());
    }

    @Test
    @DisplayName("should return book by id")
    @Order(2)
    void shouldReturnBookById() {
        BookEntity book = Assertions.assertDoesNotThrow(() -> bookService.findById(myBookId01));

        Assertions.assertNotNull(book);
        Assertions.assertEquals(myBookId01, book.getId());
        Assertions.assertEquals("Book Test 01", book.getTitle());
    }

    @Test
    @DisplayName("should return books by title")
    @Order(3)
    void shouldReturnBooksByTitle() {
        Set<BookEntity> books = Assertions.assertDoesNotThrow(() -> bookService.findByTitle("Book Test"));

        Assertions.assertNotNull(books);
        Assertions.assertEquals(2, books.size());
    }

    @Test
    @DisplayName("shouldn't return book by id and throws exception")
    @Order(4)
    void shouldNotReturnBookByIdAndThrowException() {
        Assertions.assertThrows(BookServiceException.class, () -> bookService.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("should save book")
    @Order(5)
    void shouldSaveBook() {
        BookEntity book = Assertions.assertDoesNotThrow(() -> bookService.save(new BookEntity(
                "Book Test 03",
                "Description Test 03",
                LocalDate.parse("1970-01-01"),
                "456789",
                "https://image.com/book-test-03.jpg",
                5,
                Condition.GOOD,
                CoverType.HARDCOVER
        )));

        Assertions.assertNotNull(book);
        Assertions.assertEquals("Book Test 03", book.getTitle());
    }

    @Test
    @DisplayName("should update book")
    @Order(6)
    void shouldUpdateBook() {
        BookEntity book = Assertions.assertDoesNotThrow(() -> bookService.findById(myBookId02));
        Assertions.assertNotNull(book);

        AuthorEntity newAuthor = authorRepository.save(new AuthorEntity("Test Author", "Test Bio", "https://image.test"));

        book.setTitle("Book Test 02 - (Edited)");
        book.getAuthors().add(newAuthor);

        BookEntity updatedBook = Assertions.assertDoesNotThrow(() -> bookService.update(myBookId02, book));

        Assertions.assertNotNull(updatedBook);
        Assertions.assertEquals("Book Test 02 - (Edited)", updatedBook.getTitle());
        Assertions.assertEquals("654321", updatedBook.getIsbn());
        Assertions.assertEquals(10, updatedBook.getStock());
        Assertions.assertEquals(2, updatedBook.getAuthors().size());
    }

    @Test
    @DisplayName("shouldn't update book and throws exception")
    @Order(7)
    void shouldNotUpdateBookAndThrowException() {
        Assertions.assertThrows(BookServiceException.class, () -> bookService.update(UUID.randomUUID(), new BookEntity()));
    }

    @Test
    @DisplayName("should delete book")
    @Order(8)
    void shouldDeleteBook() {
        Assertions.assertDoesNotThrow(() -> bookService.delete(myBookId01));

        Assertions.assertThrows(BookServiceException.class, () -> bookService.findById(myBookId01));
    }
}
