package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.dto.UserRegisterDto;
import bg.rborisov.softunigraduation.dto.UserWelcomeDto;
import bg.rborisov.softunigraduation.exception.UserWithUsernameOrEmailExists;
import bg.rborisov.softunigraduation.model.User;
import bg.rborisov.softunigraduation.util.JwtProvider;
import jakarta.servlet.http.Cookie;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.USER_NOT_FOUND;
import static bg.rborisov.softunigraduation.common.ExceptionMessages.USER_WITH_USERNAME_OR_EMAIL_EXISTS;
import static bg.rborisov.softunigraduation.common.JwtConstants.JWT_COOKIE_NAME;
import static bg.rborisov.softunigraduation.constant.SecurityConstant.COOKIE_MAX_AGE;
import static org.springframework.http.HttpStatus.OK;

@Service
public class UserService {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(JwtProvider jwtProvider, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, ModelMapper modelMapper, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<UserWelcomeDto> login(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);


        UserWelcomeDto userWelcomeDto = modelMapper.map(user, UserWelcomeDto.class);
        return new ResponseEntity<>(userWelcomeDto, OK);
    }

    public Cookie generateJwtCookie() {
        String token = jwtProvider.generateToken();
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, token);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        return cookie;
    }

    public UserRegisterDto register(UserRegisterDto registerDto) throws UserWithUsernameOrEmailExists {
        preRegisterValidation(registerDto);
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        User user = modelMapper.map(registerDto, User.class);
        user.setUserId(RandomStringUtils.randomAscii(10).replaceAll("\s", ""));
        user.setJoinDate(new Date());
        userRepository.save(user);

        return registerDto;
    }

    private void preRegisterValidation(UserRegisterDto registerDto) throws UserWithUsernameOrEmailExists {
        String username = registerDto.getUsername();
        String email = registerDto.getEmail();

        Optional<User> userByUsernameOrEmail = userRepository.findByUsernameOrEmail(username, email);

        if (userByUsernameOrEmail.isPresent()) {
            throw new UserWithUsernameOrEmailExists(USER_WITH_USERNAME_OR_EMAIL_EXISTS);
        }
    }
}