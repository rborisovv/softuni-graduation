package bg.rborisov.softunigraduation.model;

import bg.rborisov.softunigraduation.enumeration.VoucherTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private VoucherTypeEnum type;

    @Column(nullable = false, updatable = false)
    private BigDecimal discount;

    @ManyToMany
    private Set<User> users;

    @Column(nullable = false, updatable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @ManyToMany
    private Set<Basket> orders;
}