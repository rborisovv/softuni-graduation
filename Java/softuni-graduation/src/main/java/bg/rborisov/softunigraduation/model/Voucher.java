package bg.rborisov.softunigraduation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vouchers")
public class Voucher extends BaseEntity implements Serializable {
    @Column(nullable = false, updatable = false)
    private String type;

    @Column(nullable = false, updatable = false)
    private BigDecimal discount;

    @ManyToMany
    private Set<User> users;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationTime;

    @Column(nullable = false)
    private LocalDateTime expireTime;

    @ManyToMany
    private Set<Order> orders;
}