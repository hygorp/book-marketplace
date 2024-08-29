package org.hygorp.bookmarketplace.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tb_publisher")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class PublisherEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String logo;

    @OneToMany(mappedBy = "publisher", fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"publisher"})
    Set<BookEntity> books = new HashSet<>();

    public PublisherEntity(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }
}
