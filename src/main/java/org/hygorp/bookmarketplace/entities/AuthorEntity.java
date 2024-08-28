package org.hygorp.bookmarketplace.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tb_author")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class AuthorEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String biography;

    @Column(columnDefinition = "TEXT")
    private String image;

    @ManyToMany(mappedBy = "authors")
    private Set<BookEntity> books = new HashSet<>();

    public AuthorEntity(String name, String biography, String image) {
        this.name = name;
        this.biography = biography;
        this.image = image;
    }
}
