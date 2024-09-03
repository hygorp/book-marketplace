package org.hygorp.bookmarketplace.repositories;

import org.hygorp.bookmarketplace.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {
}
