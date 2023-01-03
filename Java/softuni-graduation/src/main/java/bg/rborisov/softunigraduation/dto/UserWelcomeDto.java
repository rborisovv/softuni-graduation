package bg.rborisov.softunigraduation.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UserWelcomeDto implements Serializable {
    private String username;
    private String email;
    private String roleName;
}