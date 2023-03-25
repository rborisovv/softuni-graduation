package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.PasswordChangeDto;
import bg.rborisov.softunigraduation.dto.UserLoginDto;
import bg.rborisov.softunigraduation.dto.UserRegisterDto;
import bg.rborisov.softunigraduation.dto.UserWelcomeDto;
import bg.rborisov.softunigraduation.exception.AbsentPasswordTokenException;
import bg.rborisov.softunigraduation.exception.PasswordTokenExpiredException;
import bg.rborisov.softunigraduation.exception.UserWithUsernameOrEmailExists;
import bg.rborisov.softunigraduation.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

import static bg.rborisov.softunigraduation.common.JwtConstants.JWT_COOKIE_NAME;

@RestController
@RequestMapping("/auth")
public class AuthResource {
    private final UserService userService;

    public AuthResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserWelcomeDto> login(@RequestParam String username,
                                                @RequestParam String password,
                                                HttpServletResponse response) throws IOException {
        @Valid UserLoginDto userLoginDto = UserLoginDto.builder().username(username).password(password).build();
        ResponseEntity<UserWelcomeDto> responseEntity = userService.login(userLoginDto);

        response.addCookie(userService.generateJwtCookie());

        return responseEntity;
    }

    @PostMapping("/register")
    public UserRegisterDto register(@RequestBody @Valid UserRegisterDto userRegisterDto) throws UserWithUsernameOrEmailExists {
        return userService.register(userRegisterDto);
    }

    @Cacheable("isAdmin")
    @GetMapping("/admin")
    @PreAuthorize("#principal.name == 'radi2000' || #principal.name == 'admin'")
    public boolean adminPage(HttpServletRequest request, Principal principal) {
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
    public void obtainCsrfToken() {

    }

    @PostMapping("/resetPassword")
    public ResponseEntity<HttpResponse> resetPassword(final @RequestBody String email) {
        return this.userService.resetPassword(email);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<HttpResponse> changePassword(final @Valid @RequestBody PasswordChangeDto passwordChangeDto) throws AbsentPasswordTokenException, PasswordTokenExpiredException {
        return this.userService.changePassword(passwordChangeDto);
    }

    @PostMapping("/hasActivePasswordRequest")
    public Boolean hasActivePasswordRequest(final @RequestBody String token) {
        return this.userService.hasActivePasswordRequest(token);
    }

    @PostMapping("/logout")
    public void logout() {
        this.userService.logout();
    }
}