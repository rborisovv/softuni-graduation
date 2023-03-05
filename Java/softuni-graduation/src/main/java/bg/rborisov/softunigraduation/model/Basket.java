package bg.rborisov.softunigraduation.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "baskets")
public class Basket extends BaseEntity implements Serializable {
    @OneToOne(mappedBy = "basket")
    private User user;

    @ManyToMany
    @ColumnDefault("null")
    private Set<Product> products;

    @ElementCollection
    private Map<Product, Integer> productQuantity;

    private String creationDate;
}