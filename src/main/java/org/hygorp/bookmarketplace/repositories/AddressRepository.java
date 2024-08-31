package org.hygorp.bookmarketplace.repositories;

import org.hygorp.bookmarketplace.entities.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {
}
