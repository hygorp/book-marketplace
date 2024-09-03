package org.hygorp.bookmarketplace.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tb_cart")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class CartEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_cart_items",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private Set<BookEntity> items = new HashSet<>();

    public Double totalValue() {
        if (items == null || items.isEmpty())
            return 0.0;

        return this.items.stream().mapToDouble(BookEntity::getPrice).sum();
    }

    public void addItem(BookEntity item) {
        items.add(item);
    }

    public void removeItem(BookEntity item) {
        items.remove(item);
    }

    public void clear() {
        items.clear();
    }
}
