package bg.rborisov.softunigraduation.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends BaseEntity implements Serializable {
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, unique = true, updatable = false)
    private String identifier;
    @Column
    private String description;
    @Column
    private LocalDate bestBefore;
    @Column(nullable = false)
    private BigDecimal price;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Media Media;
    @Column(nullable = false)
    private Integer stockLevel;
    @Column(nullable = false)
    private Boolean showBuyButton;
    @ManyToOne
    private Category category;
    @Column
    private LocalDate creationTime;
}