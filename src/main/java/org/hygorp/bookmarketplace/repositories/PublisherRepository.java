package org.hygorp.bookmarketplace.repositories;

import lombok.NonNull;
import org.hygorp.bookmarketplace.entities.PublisherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity, UUID> {
    @NonNull
    Page<PublisherEntity> findAll(@NonNull Pageable pageable);

    @NonNull
    Set<PublisherEntity> findAllByNameContainingIgnoreCase(@NonNull String name);
}
