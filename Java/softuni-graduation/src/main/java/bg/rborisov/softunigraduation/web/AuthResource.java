package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.dto.UserLoginDto;
import bg.rborisov.softunigraduation.dto.UserRegisterDto;
import bg.rborisov.softunigraduation.dto.UserWelcomeDto;
import bg.rborisov.softunigraduation.exception.ExceptionHandler;
import bg.rborisov.softunigraduation.exception.UserWithUsernameOrEmailExists;
import bg.rborisov.softunigraduation.service.UserService;
import bg.rborisov.softunigraduation.util.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static bg.rborisov.softunigraduation.common.JwtConstants.JWT_COOKIE_NAME;
import static bg.rborisov.softunigraduation.common.JwtConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/user")
public class AuthResource extends ExceptionHandler {
    private final UserService userService;

    private final JwtProvider jwtProvider;

    public AuthResource(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<UserWelcomeDto> login(@RequestParam String username,
                                                @RequestParam String password,
                                                HttpServletResponse response) {
        @Valid UserLoginDto userLoginDto = UserLoginDto.builder().username(username).password(password).build();
        ResponseEntity<UserWelcomeDto> responseEntity = userService.login(userLoginDto);

        response.addCookie(userService.generateJwtCookie());

        return responseEntity;
    }

    @PostMapping("/register")
    public UserRegisterDto register(@RequestBody @Valid UserRegisterDto userRegisterDto) throws UserWithUsernameOrEmailExists {
        return userService.register(userRegisterDto);
    }

    @PostMapping("/logout")
    public void logout() {
        this.userService.logout();
    }

    @GetMapping("/admin")
    public boolean adminPage(HttpServletRequest request) {
        String authorizationHeaders = request.getHeader(JWT_COOKIE_NAME);

        return this.userService.isAdmin(authorizationHeaders);
    }
}