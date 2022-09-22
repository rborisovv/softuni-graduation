package bg.rborisov.softunigraduation.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UserWelcomeDto {
    private String username;

    private String email;

    private Date birthDate;

    private String firstName;

    private String lastName;
}