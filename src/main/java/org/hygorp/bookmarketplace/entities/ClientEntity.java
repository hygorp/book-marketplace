package org.hygorp.bookmarketplace.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tb_client")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class ClientEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    private UserEntity credentials;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tb_client_address",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private Set<AddressEntity> addresses = new HashSet<>();

    public ClientEntity(String name, String email, String phone, UserEntity credentials) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.credentials = credentials;
    }
}
