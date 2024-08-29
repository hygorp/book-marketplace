package org.hygorp.bookmarketplace.resources;

import org.hygorp.bookmarketplace.entities.BookEntity;
import org.hygorp.bookmarketplace.services.BookService;
import org.hygorp.bookmarketplace.services.exceptions.BookServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
public class BookResource {
    private final BookService bookService;

    public BookResource(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<Page<BookEntity>> findAll(Pageable pageable) {
        Page<BookEntity> pageableBooks = bookService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(pageableBooks);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<BookEntity> findById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok().body(bookService.findById(id));
        } catch (BookServiceException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/find-by-title")
    public ResponseEntity<Set<BookEntity>> findByTitle(@RequestParam(value = "title") String title) {
        Set<BookEntity> books = bookService.findByTitle(title);

        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @PostMapping("/save")
    public ResponseEntity<BookEntity> save(@RequestBody BookEntity book) {
        try {
            BookEntity savedBook = bookService.save(book);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
        } catch (BookServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BookEntity> update(@PathVariable UUID id, @RequestBody BookEntity book) {
        try {
            BookEntity updatedBook = bookService.update(id, book);

            return ResponseEntity.status(HttpStatus.OK).body(updatedBook);
        } catch (BookServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BookEntity> delete(@PathVariable UUID id) {
        try {
            bookService.delete(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (BookServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
