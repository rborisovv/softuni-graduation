package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.CategoryRepository;
import bg.rborisov.softunigraduation.dao.PasswordTokenRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.dao.VoucherRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.*;
import bg.rborisov.softunigraduation.enumeration.VoucherTypeEnum;
import bg.rborisov.softunigraduation.exception.*;
import bg.rborisov.softunigraduation.model.*;
import bg.rborisov.softunigraduation.util.JwtProvider;
import com.github.javafaker.Faker;
import jakarta.servlet.http.Cookie;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static bg.rborisov.softunigraduation.common.JwtConstants.JWT_COOKIE_NAME;
import static bg.rborisov.softunigraduation.common.JwtConstants.TOKEN_PREFIX;
import static bg.rborisov.softunigraduation.common.Messages.*;
import static bg.rborisov.softunigraduation.constant.SecurityConstant.COOKIE_MAX_AGE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthServiceApplicationTests {

    @Mock
    private UserLoginDto userLoginDto;
    @Mock
    private UserRegisterDto userRegisterDto;
    @Mock
    private User user;
    @Autowired
    public UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private Validator validator;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private AuthenticationManager authenticationManager;
    @Autowired
    private Faker faker;
    @Autowired
    private AuthService authService;
    @MockBean
    private PasswordTokenRepository passwordTokenRepository;
    @Mock
    private PasswordChangeDto passwordChangeDto;
    @Mock
    private PasswordToken passwordToken;
    @MockBean
    private VoucherRepository voucherRepository;
    private VoucherDto voucherDto;
    @MockBean
    private CategoryRepository categoryRepository;

    @BeforeEach
    void init() {
        String registerDtoPassword = this.faker.internet().password();

        this.userRegisterDto = new UserRegisterDto("radi2000", this.faker.internet().emailAddress(),
                this.faker.name().firstName(), this.faker.name().lastName(), LocalDate.now(),
                registerDtoPassword, registerDtoPassword);

        this.userLoginDto = new UserLoginDto("radi2000", "1234567890");

        this.user = new User();

        this.user.setUserId(faker.idNumber().valid());
        this.user.setUsername("radi2000");
        this.user.setIsLocked(false);
        this.user.setRole(Role.builder().name("USER").authorities(new HashSet<>()).build());
        this.user.setJoinDate(LocalDate.now());
        this.user.setBasket(null);
        this.user.setOrder(null);
        this.user.setBirthDate(LocalDate.now());
        this.user.setEmail(faker.internet().emailAddress());
        this.user.setFavouriteProducts(new HashSet<>());
        this.user.setFirstName(faker.name().firstName());
        this.user.setLastName(faker.name().lastName());
        this.user.setImageUrl(faker.internet().image());
        this.user.setPassword("123456");
        this.user.setVouchers(new HashSet<>());

        String newPassword = faker.internet().password();

        this.passwordChangeDto = new PasswordChangeDto(newPassword, newPassword, "token");

        this.passwordToken = new PasswordToken();

        this.passwordToken.setUser(user);
        this.passwordToken.setToken(UUID.randomUUID().toString());
        this.passwordToken.setId(1L);
        this.passwordToken.setExpireDate(LocalDateTime.now().minusMinutes(30));

        this.voucherDto = new VoucherDto("voucher", VoucherTypeEnum.ABSOLUTE.name(), BigDecimal.TEN, LocalDate.now(),
                LocalDate.now().plusDays(1), new UserDto(), new CategoryDto());
    }

    @Test
    void shouldThrowWhereThereAreViolationsOnLogin() {
        Set<ConstraintViolation<UserLoginDto>> violations = new HashSet<>();

        ConstraintViolation<UserLoginDto> violation =
                ConstraintViolationImpl.forBeanValidation("Invalid email format", null,
                        null, "email", UserLoginDto.class, this.userLoginDto,
                        null, null, null, null, null);
        violations.add(violation);

        Mockito.when(this.validator.validate(this.userLoginDto)).thenReturn(violations);

        assertEquals(1, violations.size());
        assertThrows(ConstraintViolationException.class, () -> this.userService.login(this.userLoginDto));
    }

    @Test
    void shouldSuccessfullyLoginUser() throws IOException {
        Mockito.when(this.userRepository.findByUsername("radi2000")).thenReturn(Optional.of(this.user));

        UserDetails userDetailsEntity = new AppUserDetailsService(this.userRepository,
                new LoginAttemptService(null)).userDetails(user);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetailsEntity, this.user.getPassword(), new HashSet<>());

        Authentication authentication = this.authenticationManager.authenticate(authToken);

        Mockito.when(this.authenticationManager.authenticate(authToken)).thenReturn(authentication);

        ResponseEntity<UserWelcomeDto> response = this.userService.login(this.userLoginDto);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(this.user.getUsername(), Objects.requireNonNull(response.getBody()).getUsername());
    }

    @Test
    void shouldSuccessfullyLogoutUser() throws IOException {
        Mockito.when(this.userRepository.findByUsername("radi2000")).thenReturn(Optional.of(this.user));

        UserDetails userDetailsEntity = new AppUserDetailsService(this.userRepository,
                new LoginAttemptService(null)).userDetails(user);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetailsEntity, this.user.getPassword(), new HashSet<>());

        Mockito.when(this.authenticationManager.authenticate(authToken)).thenReturn(authToken);
        Authentication authentication = this.authenticationManager.authenticate(authToken);


        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        this.userService.login(this.userLoginDto);

        assertNotNull(context.getAuthentication());

        SecurityContextHolder.clearContext();

        this.userService.logout();
        context.setAuthentication(null);

        assertNull(context.getAuthentication());
    }

    @Test
    void shouldThrowViolationExceptionUponRegister() {
        Set<ConstraintViolation<UserRegisterDto>> violations = new HashSet<>();

        ConstraintViolation<UserRegisterDto> violation =
                ConstraintViolationImpl.forBeanValidation("Invalid email format", null,
                        null, "email", UserRegisterDto.class, this.userRegisterDto,
                        null, null, null, null, null);
        violations.add(violation);

        Mockito.when(this.validator.validate(this.userRegisterDto)).thenReturn(violations);

        assertThrows(ConstraintViolationException.class, () -> this.userService.register(this.userRegisterDto));
    }

    @Test
    void shouldThrowWhenUserWithSameUsernamePresent() {
        Mockito.when(this.userRepository.findByUsernameOrEmail(this.userRegisterDto.getUsername(),
                this.userRegisterDto.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(UserWithUsernameOrEmailExists.class, () -> this.userService.register(this.userRegisterDto));
    }

    @Test
    void shouldSuccessfullyRegister() throws UserWithUsernameOrEmailExists {
        final UserRegisterDto registeredUser = this.userService.register(this.userRegisterDto);
        assertEquals(registeredUser, this.userRegisterDto, "Registered user is not equal!");
    }

    @Test
    void shouldReturnUserByEmailIsPresent() {
        Mockito.when(this.userRepository.findUserByEmail("email")).thenReturn(Optional.of(new User()));
        assertTrue(this.userService.isUserWithEmailPresent("email"), "Did not return user by email!");
    }

    @Test
    void shouldReturnUserByUsernameIsPresent() {
        Mockito.when(this.userRepository.findByUsername("username")).thenReturn(Optional.of(new User()));
        assertTrue(this.userService.isUserWithUsernamePresent("username"), "Did not return user by username!");
    }

    @Test
    void shouldThrowIfNoUserByUsername() {
        Mockito.when(this.userRepository.findByUsername("radi2000")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> this.userService.findUserByUsername("radi2000"));
    }

    @Test
    void shouldSuccessfullyGenerateJwtCookie() throws RsaKeyIntegrityViolationException, IOException, NoSuchAlgorithmException {
        Mockito.when(this.jwtProvider.generateToken()).thenReturn("token");
        final Cookie cookie = this.userService.generateJwtCookie();

        assertNotNull(cookie);
        assertEquals(cookie.getName(), JWT_COOKIE_NAME);
        assertEquals(cookie.getMaxAge(), COOKIE_MAX_AGE);
    }

    @Test
    void shouldCheckIfUserIsAdmin() {
        this.user.setRole(Role.builder().name("ADMIN").build());
        Mockito.when(this.jwtProvider.getSubject("token")).thenReturn("radi2000");
        Mockito.when(this.userRepository.findByUsername("radi2000")).thenReturn(Optional.of(this.user));

        boolean isAdmin = this.authService.isAdmin(TOKEN_PREFIX + "token");

        assertTrue(isAdmin);
    }

    @Test
    void shouldSendResetPasswordRequest() {
        String email = "radii2000@abv.bg";
        Mockito.when(this.userRepository.findUserByEmail(email)).thenReturn(Optional.of(this.user));

        Mockito.when(this.passwordTokenRepository.findPasswordTokenByUser(this.user)).thenReturn(Optional.of(passwordToken));
        ResponseEntity<HttpResponse> response = this.authService.resetPassword(email);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(String.format(PASSWORD_RESET_EMAIL, email), Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void shouldThrowIfNotPasswordTokenIsPresentWhenAttemptingToChangePassword() {
        Mockito.when(this.passwordTokenRepository.findByToken("token")).thenReturn(Optional.empty());
        assertThrows(AbsentPasswordTokenException.class, () -> this.authService.changePassword(this.passwordChangeDto));
    }

    @Test
    void shouldThrowWhenPasswordResetTokenIsExpired() {
        this.passwordToken.setExpireDate(LocalDateTime.now().minusMinutes(15));
        Mockito.when(this.passwordTokenRepository.findByToken("token")).thenReturn(Optional.of(this.passwordToken));

        assertThrows(PasswordTokenExpiredException.class, () -> this.authService.changePassword(this.passwordChangeDto));
    }

    @Test
    void shouldSuccessfullyChangePassword() throws AbsentPasswordTokenException, PasswordTokenExpiredException {
        this.passwordToken.setExpireDate(LocalDateTime.now().plusMinutes(30));
        Mockito.when(this.passwordTokenRepository.findByToken("token")).thenReturn(Optional.of(this.passwordToken));
        ResponseEntity<HttpResponse> response = this.authService.changePassword(this.passwordChangeDto);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(PASSWORD_CHANGED_SUCCESSFULLY, Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void ShouldReturnIfThereAreActivePasswordRequests() {
        Mockito.when(this.passwordTokenRepository.findByToken("token")).thenReturn(Optional.empty());

        Boolean hasNoActivePasswordRequest = this.authService.hasActivePasswordRequest("token");
        assertFalse(hasNoActivePasswordRequest);

        this.passwordToken.setExpireDate(LocalDateTime.now().plusMinutes(15));
        Mockito.when(this.passwordTokenRepository.findByToken("token")).thenReturn(Optional.of(this.passwordToken));

        Boolean hasActivePasswordRequest = this.authService.hasActivePasswordRequest("token");
        assertTrue(hasActivePasswordRequest);
    }

    @Test
    void shouldLoadAllUsers() {
        List<User> users = List.of(this.user, this.user, this.user);
        this.userRepository.saveAll(users);

        Mockito.when(this.userRepository.findAll()).thenReturn(users);
        assertEquals(3, this.authService.loadAllUsers().size());
    }

    @Test
    void shouldReturnAllVouchers() {
        Voucher voucher = new Voucher();
        voucher.setName(faker.name().name());
        voucher.setCreationDate(LocalDate.now().minusDays(2));
        List<Voucher> vouchers = List.of(voucher, voucher);

        this.voucherRepository.saveAll(vouchers);

        Mockito.when(this.voucherRepository.findAll()).thenReturn(vouchers);
        assertEquals(2, this.authService.fetchAllVouchers().size());
    }

    @Test
    void shouldThrowWhenVoucherWithThatNameAlreadyPresent() {
        Mockito.when(this.voucherRepository.findVoucherByName("voucher")).thenReturn(Optional.of(new Voucher()));
        assertThrows(VoucherByNameAlreadyPresent.class, () -> this.authService.createVoucher(this.voucherDto));
    }

    @Test
    void shouldSuccessfullyCreateVoucher() throws UserNotFoundException, VoucherByNameAlreadyPresent {
        Mockito.when(this.voucherRepository.findVoucherByName("voucher")).thenReturn(Optional.empty());
        assert this.voucherDto.getUser() != null;
        this.user.setVouchers(null);
        Mockito.when(this.userRepository.findByUsername(null)).thenReturn(Optional.of(this.user));
        Mockito.when(this.categoryRepository.findCategoryByIdentifier(null)).thenReturn(Optional.of(new Category()));

        ResponseEntity<HttpResponse> response = this.authService.createVoucher(this.voucherDto);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(String.format(VOUCHER_CREATED_SUCCESSFULLY, this.voucherDto.getName()),
                Objects.requireNonNull(response.getBody()).getMessage());

    }
}