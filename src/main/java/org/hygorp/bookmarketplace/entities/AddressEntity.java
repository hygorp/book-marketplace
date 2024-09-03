package org.hygorp.bookmarketplace.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity(name = "tb_address")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AddressEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String addressLine;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String zip;

    @Column(nullable = false)
    private String country;

    @Column(columnDefinition = "TEXT")
    private String complement;

    public AddressEntity(String addressLine, String city, String state, String zip, String country, String complement) {
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.complement = complement;
    }
}
