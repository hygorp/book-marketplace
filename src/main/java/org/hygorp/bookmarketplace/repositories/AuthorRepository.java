package org.hygorp.bookmarketplace.repositories;

import lombok.NonNull;
import org.hygorp.bookmarketplace.entities.AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, UUID> {
    @NonNull
    Page<AuthorEntity> findAll(@NonNull Pageable pageable);

    @NonNull
    Set<AuthorEntity> findAllByNameContainingIgnoreCase(@NonNull String name);
}
