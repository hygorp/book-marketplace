package org.hygorp.bookmarketplace.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hygorp.bookmarketplace.enums.Condition;
import org.hygorp.bookmarketplace.enums.CoverType;
import org.hygorp.bookmarketplace.enums.Language;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tb_book")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class BookEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate publishedDate;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String image;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Condition condition;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CoverType coverType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Language language;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @JsonIgnoreProperties(value = {"books"})
    private Set<AuthorEntity> authors = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonIgnoreProperties(value = {"books"})
    private Set<GenreEntity> genres = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher_id")
    @JsonIgnoreProperties(value = {"books"})
    private PublisherEntity publisher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id")
    @JsonIgnoreProperties(value = {"books"})
    private SellerEntity seller;

    public BookEntity(
            String title,
            String description,
            LocalDate publishedDate,
            String isbn,
            String image,
            Double price,
            Integer stock,
            Condition condition,
            CoverType coverType,
            Language language
    ) {
        this.title = title;
        this.description = description;
        this.publishedDate = publishedDate;
        this.isbn = isbn;
        this.image = image;
        this.price = price;
        this.stock = stock;
        this.condition = condition;
        this.coverType = coverType;
        this.language = language;
    }
}
