package org.hygorp.bookmarketplace.services;

import org.hygorp.bookmarketplace.entities.SellerEntity;
import org.hygorp.bookmarketplace.records.Seller;
import org.hygorp.bookmarketplace.repositories.SellerRepository;
import org.hygorp.bookmarketplace.services.exceptions.SellerServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SellerService {
    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public Page<Seller> findAll(Pageable pageable) {
        Page<SellerEntity> sellers = sellerRepository.findAll(pageable);

        List<Seller> publicSellers = sellers.getContent().stream()
                .map(sellerEntity -> new Seller(
                        sellerEntity.getId(),
                        sellerEntity.getName(),
                        sellerEntity.getPhone(),
                        sellerEntity.getLogo(),
                        sellerEntity.getAddress(),
                        sellerEntity.getBooks()
                ))
                .toList();

        return new PageImpl<>(publicSellers, pageable, sellers.getTotalElements());
    }

    public SellerEntity findById(UUID id) {
        try {
           return sellerRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Seller not found"));
        } catch (NoSuchElementException exception) {
            throw new SellerServiceException("Seller not found with provided id: #" + id);
        }
    }

    public Set<SellerEntity> findByName(String name) {
        return sellerRepository.findAllByNameContainingIgnoreCase(name);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public SellerEntity save(SellerEntity seller) {
        if (sellerRepository.findAllByNameContainingIgnoreCase(seller.getName()).isEmpty()) {
            return sellerRepository.save(seller);
        }

        throw new SellerServiceException("Seller already exists");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public SellerEntity update(UUID id, SellerEntity seller) {
        try {
            SellerEntity savedSeller = sellerRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Seller not found"));

            if (!savedSeller.getName().equals(seller.getName())) {
                savedSeller.setName(seller.getName());
            }

            if (!savedSeller.getPhone().equals(seller.getPhone())) {
                savedSeller.setPhone(seller.getPhone());
            }

            if (!savedSeller.getLogo().equals(seller.getLogo())) {
                savedSeller.setLogo(seller.getLogo());
            }

            if (!savedSeller.getAddress().getAddressLine().equals(seller.getAddress().getAddressLine())) {
                savedSeller.getAddress().setAddressLine(seller.getAddress().getAddressLine());
            }

            if (!savedSeller.getAddress().getCity().equals(seller.getAddress().getCity())) {
                savedSeller.getAddress().setCity(seller.getAddress().getCity());
            }

            if (!savedSeller.getAddress().getState().equals(seller.getAddress().getState())) {
                savedSeller.getAddress().setState(seller.getAddress().getState());
            }

            if (!savedSeller.getAddress().getCountry().equals(seller.getAddress().getCountry())) {
                savedSeller.getAddress().setCountry(seller.getAddress().getCountry());
            }

            if (seller.getAddress().getComplement() != null) {
                if (!savedSeller.getAddress().getComplement().equals(seller.getAddress().getComplement())) {
                    savedSeller.getAddress().setComplement(seller.getAddress().getComplement());
                }
            }

            return sellerRepository.save(savedSeller);
        } catch (NoSuchElementException exception) {
            throw new SellerServiceException("Seller not found with provided id: #" + id);
        }
    }

    public void delete(UUID id) {
        sellerRepository.deleteById(id);
    }
}
