package org.hygorp.bookmarketplace.records;

import org.hygorp.bookmarketplace.entities.AddressEntity;
import org.hygorp.bookmarketplace.entities.BookEntity;

import java.util.Set;
import java.util.UUID;

public record Seller(UUID id, String name, String phone, String logo, AddressEntity address, Set<BookEntity> books) {
}
