package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.dto.UserLoginDto;
import bg.rborisov.softunigraduation.dto.UserRegisterDto;
import bg.rborisov.softunigraduation.dto.UserWelcomeDto;
import bg.rborisov.softunigraduation.exception.ExceptionHandler;
import bg.rborisov.softunigraduation.exception.UserWithUsernameOrEmailExists;
import bg.rborisov.softunigraduation.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/user")
public class AuthResource extends ExceptionHandler {
    private final UserService userService;

    public AuthResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserWelcomeDto> login(@RequestParam String username,
                                                @RequestParam String password,
                                                HttpServletResponse response) {
        UserLoginDto userLoginDto = UserLoginDto.builder().username(username).password(password).build();
        ResponseEntity<UserWelcomeDto> responseEntity = userService.login(userLoginDto);
        response.addCookie(userService.generateJwtCookie());
        return responseEntity;
    }

    @PostMapping("/register")
    public UserRegisterDto register(@RequestParam String username,
                                    @RequestParam String email,
                                    @RequestParam String firstName,
                                    @RequestParam String lastName,
                                    @RequestParam String birthDate,
                                    @RequestParam String password,
                                    @RequestParam String confirmPassword) throws UserWithUsernameOrEmailExists {
        UserRegisterDto userRegisterDto = new UserRegisterDto(username, email, firstName, lastName,
                LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")), password, confirmPassword);
        return userService.register(userRegisterDto);
    }
}