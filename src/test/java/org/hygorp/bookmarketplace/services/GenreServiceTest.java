package org.hygorp.bookmarketplace.services;

import org.hygorp.bookmarketplace.entities.GenreEntity;
import org.hygorp.bookmarketplace.repositories.GenreRepository;
import org.hygorp.bookmarketplace.services.exceptions.GenreServiceException;
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
@DisplayName("GenreServiceTests")
public class GenreServiceTest {
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreService genreService;

    private UUID myGenreId01;
    private UUID myGenreId02;

    @BeforeEach
    void beforeEach() {
        genreRepository.deleteAll();

        myGenreId01 = genreRepository.save(
                new GenreEntity("Fiction", "https://image.com/fiction.jpg")
        ).getId();

        myGenreId02 = genreRepository.save(
                new GenreEntity("Romance", "https://image.com/romance.jpg")
        ).getId();
    }

    @AfterEach
    void afterEach() {
        genreRepository.deleteAll();
    }

    @Test
    @DisplayName("should return all genres")
    @Order(1)
    void shouldReturnAllGenres() {
        Page<GenreEntity> genres = Assertions.assertDoesNotThrow(() -> genreService.findAll(Pageable.ofSize(10)));

        Assertions.assertNotNull(genres);
        Assertions.assertEquals(2, genres.getTotalElements());
    }

    @Test
    @DisplayName("should return genre by id")
    @Order(2)
    void shouldReturnGenreById() {
        GenreEntity genre = Assertions.assertDoesNotThrow(() -> genreService.findById(myGenreId01));

        Assertions.assertNotNull(genre);
        Assertions.assertEquals(myGenreId01, genre.getId());
        Assertions.assertEquals("Fiction", genre.getName());
    }

    @Test
    @DisplayName("shouldn't return genre by id and throws exception")
    @Order(3)
    void shouldNotReturnGenreByIdAndThrowException() {
        Assertions.assertThrows(GenreServiceException.class, () -> genreService.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("should return genres by name")
    @Order(4)
    void shouldReturnGenresByName() {
        Assertions.assertDoesNotThrow(() -> {
            Set<GenreEntity> genres = Assertions.assertDoesNotThrow(() -> genreService.findByName("Fiction"));

            Assertions.assertNotNull(genres);
            Assertions.assertEquals(1, genres.size());
        });

        Assertions.assertDoesNotThrow(() -> {
            Set<GenreEntity> genres = Assertions.assertDoesNotThrow(() -> genreService.findByName("Romance"));

            Assertions.assertNotNull(genres);
            Assertions.assertEquals(1, genres.size());
        });

    }

    @Test
    @DisplayName("should save genre")
    @Order(5)
    void shouldSaveGenre() {
        GenreEntity genre = Assertions.assertDoesNotThrow(() -> genreService.save(new GenreEntity(
                "Genre Test 03",
                "https://imaga.com/genre.jpg"
        )));

        Assertions.assertNotNull(genre);
        Assertions.assertEquals("Genre Test 03", genre.getName());
    }

    @Test
    @DisplayName("should update genre")
    @Order(6)
    void shouldUpdateGenre() {
        GenreEntity genre = Assertions.assertDoesNotThrow(() -> genreService.findById(myGenreId02));
        Assertions.assertNotNull(genre);

        genre.setName("Romance - (Edited)");

        GenreEntity updatedGenre = Assertions.assertDoesNotThrow(() -> genreService.update(myGenreId02, genre));

        Assertions.assertNotNull(updatedGenre);
        Assertions.assertEquals("Romance - (Edited)", updatedGenre.getName());
    }

    @Test
    @DisplayName("shouldn't update genre and throws exception")
    @Order(7)
    void shouldNotUpdateGenreAndThrowException() {
        Assertions.assertThrows(GenreServiceException.class, () -> genreService.update(UUID.randomUUID(), new GenreEntity()));
    }

    @Test
    @DisplayName("should delete genre")
    @Order(8)
    void shouldDeleteGenre() {
        Assertions.assertDoesNotThrow(() -> genreService.delete(myGenreId01));

        Assertions.assertThrows(GenreServiceException.class, () -> genreService.findById(myGenreId01));
    }
}
