package bg.rborisov.softunigraduation.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "roles")
public class Role extends BaseEntity implements Serializable {

    @Column(nullable = false)
    @NonNull
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Authority> authorities;
}