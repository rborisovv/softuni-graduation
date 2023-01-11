package bg.rborisov.softunigraduation.model;

import bg.rborisov.softunigraduation.enumeration.MediaTypeEnum;
import jakarta.persistence.*;
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

    @Column(nullable = false, unique = true)
    private String pkOfFile;

    @OneToOne
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private MediaTypeEnum mediaSubject;
}