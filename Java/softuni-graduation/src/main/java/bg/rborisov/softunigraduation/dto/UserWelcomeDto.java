package bg.rborisov.softunigraduation.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UserWelcomeDto {
    private String username;
    private String email;
}