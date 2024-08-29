package org.hygorp.bookmarketplace.repositories;

import lombok.NonNull;
import org.hygorp.bookmarketplace.entities.GenreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, UUID> {
    @NonNull
    Page<GenreEntity> findAll(@NonNull Pageable pageable);

    @NonNull
    Set<GenreEntity> findAllByNameContainingIgnoreCase(@NonNull String genreName);
}
