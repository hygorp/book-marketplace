package org.hygorp.bookmarketplace.resources;

import org.hygorp.bookmarketplace.entities.GenreEntity;
import org.hygorp.bookmarketplace.services.GenreService;
import org.hygorp.bookmarketplace.services.exceptions.GenreServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/genres")
public class GenreResource {
    private final GenreService genreService;

    public GenreResource(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<Page<GenreEntity>> findAll(Pageable pageable) {
        Page<GenreEntity> pageableGenres = genreService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(pageableGenres);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<GenreEntity> findById(@PathVariable UUID id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(genreService.findById(id));
        } catch (GenreServiceException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("find-by-name")
    public ResponseEntity<Set<GenreEntity>> findByName(@RequestParam(name = "name") String name) {
        Set<GenreEntity> genres = genreService.findByName(name);

        return ResponseEntity.status(HttpStatus.OK).body(genres);
    }

    @PostMapping("/save")
    public ResponseEntity<GenreEntity> save(@RequestBody GenreEntity genre) {
        try {
            GenreEntity savedGenre = genreService.save(genre);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedGenre);
        } catch (GenreServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenreEntity> update(@PathVariable UUID id, @RequestBody GenreEntity genre) {
        try {
            GenreEntity updatedGenre = genreService.update(id, genre);

            return ResponseEntity.status(HttpStatus.OK).body(updatedGenre);
        } catch (GenreServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenreEntity> delete(@PathVariable UUID id) {
        try {
            genreService.delete(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (GenreServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
