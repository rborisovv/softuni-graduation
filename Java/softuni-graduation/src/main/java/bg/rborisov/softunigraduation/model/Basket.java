package bg.rborisov.softunigraduation.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

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

    @ElementCollection
    private Map<Product, Integer> productMapping;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @ManyToOne
    private Voucher voucher;
}