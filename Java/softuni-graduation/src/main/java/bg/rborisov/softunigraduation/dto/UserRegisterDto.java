package bg.rborisov.softunigraduation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UserRegisterDto {

    @NotBlank
    @Size(min = 5, max = 30)
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 3, max = 30)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 30)
    private String lastName;

    @NotNull
    @PastOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Europe/Sofia")
    private LocalDate birthDate;

    @NotBlank
    @Size(min = 6, max = 50)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank
    @Size(min = 6, max = 50)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;
}