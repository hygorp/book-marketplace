package org.hygorp.bookmarketplace.services;

import org.hygorp.bookmarketplace.entities.AuthorEntity;
import org.hygorp.bookmarketplace.repositories.AuthorRepository;
import org.hygorp.bookmarketplace.services.exceptions.AuthorServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Page<AuthorEntity> findAll(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    public AuthorEntity findById(UUID id) {
        try {
            return authorRepository.findById(id).orElseThrow(
                    () -> new NoSuchElementException("Author not found")
            );
        } catch (NoSuchElementException exception) {
            throw new AuthorServiceException("Author not found with provided id: #" + id);
        }
    }

    public Set<AuthorEntity> findByName(String name) {
        return authorRepository.findAllByNameContainingIgnoreCase(name);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public AuthorEntity save(AuthorEntity author) {
        return authorRepository.save(author);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public AuthorEntity update(UUID id, AuthorEntity author) {
        try {
            AuthorEntity savedAuthor = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Author not found"));

            if (!Objects.equals(savedAuthor.getName(), author.getName())) {
                savedAuthor.setName(author.getName());
            }

            if (!Objects.equals(savedAuthor.getBiography(), author.getBiography())) {
                savedAuthor.setBiography(author.getBiography());
            }

            if (!Objects.equals(savedAuthor.getImage(), author.getImage())) {
                savedAuthor.setImage(author.getImage());
            }

            if (!Objects.equals(savedAuthor.getBooks(), author.getBooks())) {
                savedAuthor.getBooks().addAll(author.getBooks());
            }

            return authorRepository.save(savedAuthor);
        } catch (NoSuchElementException exception) {
            throw new AuthorServiceException("Author not found with provided id: #" + id);
        }
    }

    public void delete(UUID id) {
        authorRepository.deleteById(id);
    }
}
