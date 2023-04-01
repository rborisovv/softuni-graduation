package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.*;
import bg.rborisov.softunigraduation.exception.*;
import bg.rborisov.softunigraduation.service.AuthService;
import bg.rborisov.softunigraduation.service.OrderService;
import bg.rborisov.softunigraduation.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

import static bg.rborisov.softunigraduation.common.JwtConstants.JWT_COOKIE_NAME;

@RestController
@RequestMapping("/auth")
public class AuthResource {
    private final UserService userService;
    private final AuthService authService;
    private final OrderService orderService;

    public AuthResource(UserService userService, final AuthService authService, OrderService orderService) {
        this.userService = userService;
        this.authService = authService;
        this.orderService = orderService;
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
    public boolean adminPage(HttpServletRequest request) {
        String authorizationHeaders = request.getHeader(JWT_COOKIE_NAME);
        return this.authService.isAdmin(authorizationHeaders);
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
        return this.authService.resetPassword(email);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<HttpResponse> changePassword(final @Valid @RequestBody PasswordChangeDto passwordChangeDto) throws AbsentPasswordTokenException, PasswordTokenExpiredException {
        return this.authService.changePassword(passwordChangeDto);
    }

    @PostMapping("/hasActivePasswordRequest")
    public Boolean hasActivePasswordRequest(final @RequestBody String token) {
        return this.authService.hasActivePasswordRequest(token);
    }

    @PostMapping("/logout")
    public void logout() {
        this.userService.logout();
    }

    @GetMapping("/users")
    public Set<UserDto> loadAllUsers() {
        return this.authService.loadAllUsers();
    }

    @GetMapping("/orders")
    public Set<OrderDto> fetchAllOrders() {
        return this.orderService.fetchAllOrders();
    }

    @GetMapping("/vouchers")
    public Set<VoucherDto> vouchers() {
        return this.authService.fetchAllVouchers();
    }

    @PostMapping("/createVoucher")
    public ResponseEntity<HttpResponse> createVoucher(final @Valid @RequestBody VoucherDto voucherDto) throws UserNotFoundException, VoucherByNameAlreadyPresent {
        return this.authService.createVoucher(voucherDto);
    }
}