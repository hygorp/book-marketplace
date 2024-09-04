package org.hygorp.bookmarketplace.services;

import org.hygorp.bookmarketplace.entities.ClientEntity;
import org.hygorp.bookmarketplace.repositories.ClientRepository;
import org.hygorp.bookmarketplace.services.exceptions.ClientServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientEntity findById(UUID id) {
        try {
            return clientRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Client not found"));
        } catch (NoSuchElementException exception) {
            throw new ClientServiceException("Client not found with provided id: #" + id);
        }
    }

    public ClientEntity findByUsername(String username) {
        try {
            return clientRepository.findByCredentials_Username(username).orElseThrow(() -> new NoSuchElementException("Client not found"));
        } catch (NoSuchElementException exception) {
            throw new ClientServiceException("Client not found with provided username: #" + username);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ClientEntity save(ClientEntity client) {
        if(clientRepository.existsByCpf(client.getCpf())) {
            throw new ClientServiceException("Client already exists");

        }

        return clientRepository.save(client);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ClientEntity update(UUID id, ClientEntity client) {
        try {
            ClientEntity savedClient = clientRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Client not found"));

            if (!Objects.equals(savedClient.getName(), client.getName())) {
                savedClient.setName(client.getName());
            }

            if (!Objects.equals(savedClient.getCpf(), client.getCpf())) {
                savedClient.setCpf(client.getCpf());
            }

            if (!Objects.equals(savedClient.getEmail(), client.getEmail())) {
                savedClient.setEmail(client.getEmail());
            }

            if (!Objects.equals(savedClient.getPhone(), client.getPhone())) {
                savedClient.setPhone(client.getPhone());
            }

            if (!Objects.equals(savedClient.getCredentials().getUsername(), client.getCredentials().getUsername())) {
                savedClient.getCredentials().setUsername(client.getCredentials().getUsername());
            }

            if (!Objects.equals(savedClient.getCredentials().getPassword(), client.getCredentials().getPassword())) {
                savedClient.getCredentials().setPassword(client.getCredentials().getPassword());
            }

            if (!Objects.equals(savedClient.getAddresses(), client.getAddresses())) {
                savedClient.getAddresses().addAll(client.getAddresses());
            }


            return clientRepository.save(savedClient);
        } catch (NoSuchElementException exception) {
            throw new ClientServiceException("Client not found with provided id: #" + id);
        }
    }

    public void delete(UUID id) {
        clientRepository.deleteById(id);
    }
}
