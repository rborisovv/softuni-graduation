package bg.rborisov.softunigraduation.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Europe/Sofia")
    private Date joinDate;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked;

    @Column(name = "is_expired", nullable = false)
    private Boolean isExpired;

    @Column(name = "is_disabled", nullable = false)
    private Boolean isDisabled;
}