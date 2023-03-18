package bg.rborisov.softunigraduation.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "password_reset_tokens")
public class PasswordToken extends BaseEntity implements Serializable {
    @Column(nullable = false, unique = true, updatable = false)
    private String token;
    @OneToOne
    private User user;
    @Column(nullable = false, updatable = false)
    private LocalDateTime expireDate;
}