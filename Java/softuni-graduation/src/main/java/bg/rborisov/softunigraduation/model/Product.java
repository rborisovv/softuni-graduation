package bg.rborisov.softunigraduation.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "products")
public class Product extends BaseEntity implements Serializable {
    private String name;
    private BigDecimal price;
    @OneToMany(mappedBy = "product")
    private Set<Attribute> attributes;
    @OneToOne
    private Media Media;
    @ManyToOne
    private Category categories;
    private LocalDate creationTime;
}