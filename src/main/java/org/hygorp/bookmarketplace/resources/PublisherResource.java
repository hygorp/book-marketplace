package org.hygorp.bookmarketplace.resources;

import org.hygorp.bookmarketplace.entities.PublisherEntity;
import org.hygorp.bookmarketplace.services.PublisherService;
import org.hygorp.bookmarketplace.services.exceptions.PublisherServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/publishers")
public class PublisherResource {
    private final PublisherService publisherService;

    public PublisherResource(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<Page<PublisherEntity>> findAll(Pageable pageable) {
        Page<PublisherEntity> pageablePublishers = publisherService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(pageablePublishers);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<PublisherEntity> findById(@PathVariable UUID id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(publisherService.findById(id));
        } catch (PublisherServiceException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("find-by-name")
    public ResponseEntity<Set<PublisherEntity>> findByName(@RequestParam(name = "name") String name) {
        Set<PublisherEntity> publishers = publisherService.findByName(name);

        return ResponseEntity.status(HttpStatus.OK).body(publishers);
    }

    @PostMapping("/save")
    public ResponseEntity<PublisherEntity> save(@RequestBody PublisherEntity publisher) {
        try {
            PublisherEntity savedPublisher = publisherService.save(publisher);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedPublisher);
        } catch (PublisherServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PublisherEntity> update(@PathVariable UUID id, @RequestBody PublisherEntity publisher) {
        try {
            PublisherEntity updatedPublisher = publisherService.update(id, publisher);

            return ResponseEntity.status(HttpStatus.OK).body(updatedPublisher);
        } catch (PublisherServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<PublisherEntity> delete(@PathVariable UUID id) {
        try {
            publisherService.delete(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (PublisherServiceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
