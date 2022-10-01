package bg.rborisov.softunigraduation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "cities")
public class City extends BaseEntity implements Serializable {

    @Column(nullable = false, updatable = false, unique = true)
    private String name;

    @Column(name = "image_url", nullable = false, unique = true)
    private String imageUrl;
}