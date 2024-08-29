package org.hygorp.bookmarketplace.services;

import org.hygorp.bookmarketplace.entities.GenreEntity;
import org.hygorp.bookmarketplace.repositories.GenreRepository;
import org.hygorp.bookmarketplace.services.exceptions.GenreServiceException;
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
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Page<GenreEntity> findAll(Pageable pageable) {
        return genreRepository.findAll(pageable);
    }

    public GenreEntity findById(UUID id) {
        try {
            return genreRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Genre not found"));
        } catch (NoSuchElementException exception) {
            throw new GenreServiceException("Genre not found with provided id: #" + id);
        }
    }

    public Set<GenreEntity> findByName(String name) {
        return genreRepository.findAllByNameContainingIgnoreCase(name);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public GenreEntity save(GenreEntity genre) {
        return genreRepository.save(genre);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public GenreEntity update(UUID id, GenreEntity genre) {
        try {
            GenreEntity savedGenre = genreRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Genre not found"));

            if (!Objects.equals(savedGenre.getName(), genre.getName())) {
                savedGenre.setName(genre.getName());
            }

            if (!Objects.equals(savedGenre.getImage(), genre.getImage())) {
                savedGenre.setImage(genre.getImage());
            }

            if (!Objects.equals(savedGenre.getBooks(), genre.getBooks())) {
                savedGenre.getBooks().addAll(genre.getBooks());
            }

            return genreRepository.save(savedGenre);
        } catch (NoSuchElementException exception) {
            throw new GenreServiceException("Genre not found with provided id: #" + id);
        }
    }

    public void delete(UUID id) {
        genreRepository.deleteById(id);
    }
}
