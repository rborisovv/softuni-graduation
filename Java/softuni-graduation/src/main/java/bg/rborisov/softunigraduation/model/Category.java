package bg.rborisov.softunigraduation.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category extends BaseEntity implements Serializable {
    @Column(nullable = false, unique = true, length = 40)
    private String name;
    @Column(nullable = false, unique = true, length = 10)
    private String identifier;
    @ManyToOne
    private Category superCategory;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Media media;
    @OneToMany(mappedBy = "category")
    public List<Product> products;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return identifier.equals(category.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}