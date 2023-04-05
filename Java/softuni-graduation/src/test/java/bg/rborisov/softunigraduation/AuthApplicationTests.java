package bg.rborisov.softunigraduation;

import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.dto.UserRegisterDto;
import bg.rborisov.softunigraduation.exception.RsaKeyIntegrityViolationException;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.exception.UserWithUsernameOrEmailExists;
import bg.rborisov.softunigraduation.model.User;
import bg.rborisov.softunigraduation.service.UserService;
import bg.rborisov.softunigraduation.util.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static bg.rborisov.softunigraduation.common.JwtConstants.JWT_COOKIE_NAME;
import static bg.rborisov.softunigraduation.constant.SecurityConstant.COOKIE_MAX_AGE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AuthApplicationTests {

    @Mock
    private UserRegisterDto userRegisterDto;

    @Autowired
    public UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private Validator validator;

    @MockBean
    private JwtProvider jwtProvider;

    @BeforeEach
    public void init() {
        this.userRegisterDto = new UserRegisterDto("radi2000", "radii2000@abv.bg", "Radoslav",
                "Borisov", LocalDate.now(), "123456", "123456");
    }

    @Test
    public void shouldThrowViolationExceptionUponRegister() {
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
    public void shouldThrowWhenUserWithSameUsernamePresent() {
        Mockito.when(this.userRepository.findByUsernameOrEmail(this.userRegisterDto.getUsername(),
                this.userRegisterDto.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(UserWithUsernameOrEmailExists.class, () -> this.userService.register(this.userRegisterDto));
    }

    @Test
    public void shouldSuccessfullyRegister() throws UserWithUsernameOrEmailExists {
        final UserRegisterDto registeredUser = this.userService.register(this.userRegisterDto);

        assertEquals(registeredUser, this.userRegisterDto, "Registered user is not equal!");
    }

    @Test
    public void shouldReturnUserByEmailIsPresent() {
        Mockito.when(this.userRepository.findUserByEmail("email")).thenReturn(Optional.of(new User()));
        assertTrue(this.userService.isUserWithEmailPresent("email"), "Did not return user by email!");
    }

    @Test
    public void shouldReturnUserByUsernameIsPresent() {
        Mockito.when(this.userRepository.findByUsername("username")).thenReturn(Optional.of(new User()));
        assertTrue(this.userService.isUserWithUsernamePresent("username"), "Did not return user by username!");
    }

    @Test
    public void shouldReturnUserByUsername() {
        Mockito.when(this.userRepository.findByUsername("username")).thenReturn(Optional.of(new User()));
        assertDoesNotThrow(() -> UserNotFoundException.class, "Cannot find User by username!");
    }

    @Test
    public void shouldSuccessfullyGenerateJwtCookie() throws RsaKeyIntegrityViolationException, IOException, NoSuchAlgorithmException {
        Mockito.when(this.jwtProvider.generateToken()).thenReturn("token");
        final Cookie cookie = this.userService.generateJwtCookie();

        assertNotNull(cookie);
        assertEquals(cookie.getName(), JWT_COOKIE_NAME);
        assertEquals(cookie.getMaxAge(), COOKIE_MAX_AGE);
    }
}