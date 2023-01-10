package bg.rborisov.softunigraduation.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "categories")
public class Category extends BaseEntity implements Serializable {
    @Column(nullable = false, unique = true, length = 40)
    private String name;
    @Column(nullable = false, unique = true, length = 10)
    private String identifier;
    @Column(length = 30)
    private String productNamePrefix;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Media media;
//    @OneToMany(mappedBy = "category")
//    public Set<Product> products;
}