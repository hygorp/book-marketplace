package org.hygorp.bookmarketplace.services;

import org.hygorp.bookmarketplace.entities.PublisherEntity;
import org.hygorp.bookmarketplace.repositories.PublisherRepository;
import org.hygorp.bookmarketplace.services.exceptions.PublisherServiceException;
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
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Page<PublisherEntity> findAll(Pageable pageable) {
        return publisherRepository.findAll(pageable);
    }

    public PublisherEntity findById(UUID id) {
        try {
            return publisherRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Publisher not found"));
        } catch (NoSuchElementException exception) {
            throw new PublisherServiceException("Publisher not found with provided id: #" + id);
        }
    }

    public Set<PublisherEntity> findByName(String name) {
        return publisherRepository.findAllByNameContainingIgnoreCase(name);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public PublisherEntity save(PublisherEntity publisher) {
        return publisherRepository.save(publisher);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public PublisherEntity update(UUID id, PublisherEntity publisher) {
        try {
            PublisherEntity savedPublisher = publisherRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Publisher not found"));

            if (!Objects.equals(savedPublisher.getName(), publisher.getName())) {
                savedPublisher.setName(publisher.getName());
            }

            if (!Objects.equals(savedPublisher.getLogo(), publisher.getLogo())) {
                savedPublisher.setLogo(publisher.getLogo());
            }

            if (!Objects.equals(savedPublisher.getBooks(), publisher.getBooks())) {
                savedPublisher.getBooks().addAll(publisher.getBooks());
            }

            return publisherRepository.save(savedPublisher);
        } catch (NoSuchElementException exception) {
            throw new PublisherServiceException("Publisher not found with provided id: #" + id);
        }
    }

    public void delete(UUID id) {
        publisherRepository.deleteById(id);
    }
}
