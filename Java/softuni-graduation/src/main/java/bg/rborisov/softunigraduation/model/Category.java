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
@Table(name = "categories")
public class Category extends BaseEntity implements Serializable {
    @Column(nullable = false, unique = true, length = 40)
    private String name;
    @Column(nullable = false, unique = true, length = 10)
    private String categoryIdentifier;
    @Column(length = 30)
    private String productNamePrefix;
    @Column(nullable = false)
    private String mediaUrl;
}