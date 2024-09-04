package org.hygorp.bookmarketplace.repositories;

import org.hygorp.bookmarketplace.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, UUID> {
    Optional<ClientEntity> findByCredentials_Username (String username);

    Boolean existsByCpf (String cpf);
}
