package org.hygorp.bookmarketplace.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tb_seller")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class SellerEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String logo;

    @Column(nullable = false)
    private Instant createdAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserEntity credentials;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"seller"})
    private Set<BookEntity> books = new HashSet<>();

    public SellerEntity(String name, String phone, String logo, Instant createdAt, UserEntity credentials, AddressEntity address) {
        this.name = name;
        this.phone = phone;
        this.logo = logo;
        this.createdAt = createdAt;
        this.credentials = credentials;
        this.address = address;
    }
}
