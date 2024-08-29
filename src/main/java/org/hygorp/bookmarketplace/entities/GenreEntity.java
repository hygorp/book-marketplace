package org.hygorp.bookmarketplace.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tb_genre")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class GenreEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String image;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"genres"})
    private Set<BookEntity> books = new HashSet<>();

    public GenreEntity(String name, String image) {
        this.name = name;
        this.image = image;
    }
}
