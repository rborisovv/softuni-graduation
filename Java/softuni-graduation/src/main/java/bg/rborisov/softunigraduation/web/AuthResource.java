package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.dto.UserLoginDto;
import bg.rborisov.softunigraduation.dto.UserRegisterDto;
import bg.rborisov.softunigraduation.dto.UserWelcomeDto;
import bg.rborisov.softunigraduation.exception.ExceptionHandler;
import bg.rborisov.softunigraduation.exception.UserWithUsernameOrEmailExists;
import bg.rborisov.softunigraduation.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AuthResource extends ExceptionHandler {
    private final UserService userService;

    public AuthResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserWelcomeDto> login(@RequestBody @Valid UserLoginDto userLoginDto, HttpServletResponse response) {
        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();
        ResponseEntity<UserWelcomeDto> responseEntity = userService.login(username, password);
        response.addCookie(userService.generateJwtCookie());
        return responseEntity;
    }

    @GetMapping("/register")
    public UserRegisterDto register(@RequestBody @Valid UserRegisterDto registerDto) throws UserWithUsernameOrEmailExists {
        return userService.register(registerDto);
    }
}