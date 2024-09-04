package org.hygorp.bookmarketplace.repositories;

import lombok.NonNull;
import org.hygorp.bookmarketplace.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsernameIgnoreCase(@NonNull String username);
}
