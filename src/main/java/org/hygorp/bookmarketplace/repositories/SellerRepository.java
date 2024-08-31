package org.hygorp.bookmarketplace.repositories;

import lombok.NonNull;
import org.hygorp.bookmarketplace.entities.SellerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<SellerEntity, UUID> {
    @NonNull
    Page<SellerEntity> findAll(@NonNull Pageable pageable);

    @NonNull
    Set<SellerEntity> findAllByNameContainingIgnoreCase(@NonNull String name);
}
