package org.hygorp.bookmarketplace.repositories;

import org.hygorp.bookmarketplace.entities.GenreEntity;
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
@DisplayName("GenreRepositoryTests")
public class GenreRepositoryTest {
    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName("should save genre")
    @Order(1)
    void shouldSaveGenre() {
        GenreEntity genre = Assertions.assertDoesNotThrow(() -> genreRepository.save(new GenreEntity(
                "Genre Test 01",
                "https://image.com/genre-test-01.jpg"
        )));

        Assertions.assertNotNull(genre);
        Assertions.assertEquals("Genre Test 01", genre.getName());
    }

    @Test
    @DisplayName("should find genre by id")
    @Order(2)
    void ShouldFindGenreById() {
        GenreEntity genre = Assertions.assertDoesNotThrow(() -> genreRepository.save(new GenreEntity(
                "Genre Test 01",
                "https://image.com/genre-test-01.jpg"
        )));

        UUID myGenreId = genre.getId();

        GenreEntity savedGenre = Assertions.assertDoesNotThrow(() -> genreRepository.findById(myGenreId).orElseThrow());

        Assertions.assertEquals(genre.getName(), savedGenre.getName());
    }

    @Test
    @DisplayName("should update genre")
    @Order(3)
    void shouldUpdateGenre() {
        GenreEntity genre = Assertions.assertDoesNotThrow(() -> genreRepository.save(new GenreEntity(
                "Genre Test 01",
                "https://image.com/genre-test-01.jpg"
        )));

        UUID myGenreId = genre.getId();

        GenreEntity savedGenre = Assertions.assertDoesNotThrow(() -> genreRepository.findById(myGenreId).orElseThrow());
        savedGenre.setName("Genre Test 01 (edited)");

        GenreEntity editedGenre = Assertions.assertDoesNotThrow(() -> genreRepository.save(savedGenre));

        Assertions.assertEquals(myGenreId, editedGenre.getId());
        Assertions.assertEquals("Genre Test 01 (edited)", editedGenre.getName());
    }

    @Test
    @DisplayName("should delete genre")
    @Order(4)
    void shouldDeleteGenre() {
        GenreEntity genre = Assertions.assertDoesNotThrow(() -> genreRepository.save(new GenreEntity(
                "Genre Test 01",
                "https://image.com/genre-test-01.jpg"
        )));

        UUID myGenreId = genre.getId();

        genreRepository.deleteById(myGenreId);

        Assertions.assertThrows(NoSuchElementException.class, () -> genreRepository.findById(myGenreId).orElseThrow());
    }
}
