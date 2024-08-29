package org.hygorp.bookmarketplace.services;

import org.hygorp.bookmarketplace.entities.BookEntity;
import org.hygorp.bookmarketplace.repositories.BookRepository;
import org.hygorp.bookmarketplace.services.exceptions.BookServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<BookEntity> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public BookEntity findById(UUID id) {
        try {
            return bookRepository.findById(id).orElseThrow(
                    () -> new NoSuchElementException("Book not found")
            );
        } catch (NoSuchElementException exception) {
            throw new BookServiceException("Book not found with provided id: #" + id);
        }
    }

    public Set<BookEntity> findByTitle(String title) {
        return bookRepository.findAllByTitleContainingIgnoreCase(title);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public BookEntity save(BookEntity book) {
        return bookRepository.save(book);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public BookEntity update(UUID id, BookEntity book) {
        try {
            BookEntity savedBook = bookRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Book not found"));

            if (!Objects.equals(savedBook.getTitle(), book.getTitle())) {
                savedBook.setTitle(book.getTitle());
            }

            if (!Objects.equals(savedBook.getDescription(), book.getDescription())) {
                savedBook.setDescription(book.getDescription());
            }

            if (!Objects.equals(savedBook.getPublishedDate(), book.getPublishedDate())) {
                savedBook.setPublishedDate(book.getPublishedDate());
            }

            if (!Objects.equals(savedBook.getIsbn(), book.getIsbn())) {
                savedBook.setIsbn(book.getIsbn());
            }

            if (!Objects.equals(savedBook.getImage(), book.getImage())) {
                savedBook.setImage(book.getImage());
            }

            if (!Objects.equals(savedBook.getStock(), book.getStock())) {
                savedBook.setStock(book.getStock());
            }

            if (!Objects.equals(savedBook.getCondition(), book.getCondition())) {
                savedBook.setCondition(book.getCondition());
            }

            if (!Objects.equals(savedBook.getCoverType(), book.getCoverType())) {
                savedBook.setCoverType(book.getCoverType());
            }

            if (!Objects.equals(savedBook.getAuthors(), book.getAuthors())) {
                savedBook.getAuthors().addAll(book.getAuthors());
            }

            if (!Objects.equals(savedBook.getGenres(), book.getGenres())) {
                savedBook.getGenres().addAll(book.getGenres());
            }

            return bookRepository.save(savedBook);
        } catch (NoSuchElementException e) {
            throw new BookServiceException("Book not found with provided id: #" + id);
        }
    }

    public void delete(UUID id) {
        bookRepository.deleteById(id);
    }
}
