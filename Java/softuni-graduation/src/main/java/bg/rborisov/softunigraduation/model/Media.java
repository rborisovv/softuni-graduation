package bg.rborisov.softunigraduation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
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
@Table(name = "media")
public class Media extends BaseEntity implements Serializable {
    @Column(nullable = false, unique = true)
    private String name;

    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = false)
    private byte[] file;

    @Column(nullable = false, unique = true)
    private String mediaUrl;
}