package org.hygorp.bookmarketplace.resources;

import org.hygorp.bookmarketplace.entities.AuthorEntity;
import org.hygorp.bookmarketplace.services.AuthorService;
import org.hygorp.bookmarketplace.services.exceptions.AuthorServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorResource {
    private final AuthorService authorService;

    public AuthorResource(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<Page<AuthorEntity>> findAll(Pageable pageable) {
        Page<AuthorEntity> pageableAuthors = authorService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(pageableAuthors);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<AuthorEntity> findById(@PathVariable UUID id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(authorService.findById(id));
        } catch (AuthorServiceException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("find-by-name")
    public ResponseEntity<Set<AuthorEntity>> findByName(@RequestParam(name = "name") String name) {
        Set<AuthorEntity> authors = authorService.findByName(name);

        return ResponseEntity.status(HttpStatus.OK).body(authors);
    }

    @PostMapping("/save")
    public ResponseEntity<AuthorEntity> save(@RequestBody AuthorEntity author) {
        try {
            AuthorEntity savedAuthor = authorService.save(author);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
        } catch (AuthorServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AuthorEntity> update(@PathVariable UUID id, @RequestBody AuthorEntity author) {
        try {
            AuthorEntity updatedAuthor = authorService.update(id, author);

            return ResponseEntity.status(HttpStatus.OK).body(updatedAuthor);
        } catch (AuthorServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AuthorEntity> delete(@PathVariable UUID id) {
        try {
            authorService.delete(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (AuthorServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
