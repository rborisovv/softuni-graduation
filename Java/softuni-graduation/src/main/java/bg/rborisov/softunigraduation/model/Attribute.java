package bg.rborisov.softunigraduation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
@Table(name = "attributes")
public class Attribute extends BaseEntity implements Serializable {
    private String name;

    private String value;

    @ManyToOne
    private Product product;
}