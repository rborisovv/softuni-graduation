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
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import static bg.rborisov.softunigraduation.common.JwtConstants.JWT_COOKIE_NAME;

@RestController
@RequestMapping("/auth")
public class AuthResource {
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

    @GetMapping("/admin")
    public boolean adminPage(HttpServletRequest request) {
        String authorizationHeaders = request.getHeader(JWT_COOKIE_NAME);

        return this.userService.isAdmin(authorizationHeaders);
    }

    @PostMapping("/email")
    public boolean isEmailPresent(@RequestBody String email) {
        return this.userService.isUserWithEmailPresent(email);
    }

    @PostMapping("/username")
    public boolean isUsernamePresent(@RequestBody String username) {
        return this.userService.isUserWithUsernamePresent(username);
    }

    @GetMapping("/csrf")
    public void obtainCsrfToken(HttpServletRequest request, HttpServletResponse response) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
    }

    @PostMapping("/logout")
    public void logout() {
        this.userService.logout();
    }
}