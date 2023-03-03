package bg.rborisov.softunigraduation.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity implements Serializable {

    @Column(name = "user_id", nullable = false, unique = true, updatable = false)
    private String userId;

    @Column(nullable = false, unique = true, updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToOne
    private Role role;

    @Column(name = "join_date", nullable = false, updatable = false)
    private LocalDate joinDate;

    @Column(name = "birth_date", nullable = false, updatable = false)
    private LocalDate birthDate;


    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked;

    @ManyToMany
    @ColumnDefault("null")
    private Set<Product> favouriteProducts;
    @OneToOne
    private Basket basket;
}