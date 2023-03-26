package bg.rborisov.softunigraduation.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String imageUrl;
}