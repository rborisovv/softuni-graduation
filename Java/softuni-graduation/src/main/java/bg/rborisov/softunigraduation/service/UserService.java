package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.*;
import bg.rborisov.softunigraduation.domain.FavouritesHttpResponse;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.*;
import bg.rborisov.softunigraduation.enumeration.*;
import bg.rborisov.softunigraduation.exception.*;
import bg.rborisov.softunigraduation.model.*;
import bg.rborisov.softunigraduation.util.HttpResponseConstructor;
import bg.rborisov.softunigraduation.util.JwtProvider;
import bg.rborisov.softunigraduation.util.logger.AuthLogger;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.*;
import static bg.rborisov.softunigraduation.common.JwtConstants.JWT_COOKIE_NAME;
import static bg.rborisov.softunigraduation.common.LogMessages.USER_LOGGED_IN;
import static bg.rborisov.softunigraduation.common.Messages.*;
import static bg.rborisov.softunigraduation.constant.SecurityConstant.COOKIE_MAX_AGE;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@Slf4j
public class UserService {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final Validator validator;
    private final ProductRepository productRepository;
    private final LoginAttemptService loginAttemptService;

    public UserService(JwtProvider jwtProvider, UserDetailsService userDetailsService, AuthenticationManager authenticationManager,
                       ModelMapper modelMapper, UserRepository userRepository, PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository, Validator validator, ProductRepository productRepository,
                       LoginAttemptService loginAttemptService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.validator = validator;
        this.productRepository = productRepository;
        this.loginAttemptService = loginAttemptService;
    }

    public ResponseEntity<UserWelcomeDto> login(UserLoginDto userLoginDto) throws IOException {
        Set<ConstraintViolation<UserLoginDto>> violations = validator.validate(userLoginDto);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new BadCredentialsException(USERNAME_OR_PASSWORD_INCORRECT));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        this.loginAttemptService.evictUserFromLoginAttemptCache(request);

        UserWelcomeDto userWelcomeDto = modelMapper.map(user, UserWelcomeDto.class);
        AuthLogger authLogger = new AuthLogger();
        authLogger.log(String.format(USER_LOGGED_IN, user.getUsername()), LoggerStatus.INFO);
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
        Set<ConstraintViolation<UserRegisterDto>> violations = validator.validate(registerDto);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        preRegisterValidation(registerDto);
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        User user = modelMapper.map(registerDto, User.class);
        user.setUserId(RandomStringUtils.randomAscii(10).replaceAll(StringUtils.SPACE, StringUtils.EMPTY));
        Role userRole = roleRepository.findRoleByName(RoleEnum.USER.name());
        user.setRole(userRole);
        user.setJoinDate(LocalDate.now());
        user.setIsLocked(false);
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

    public boolean isUserWithEmailPresent(String email) {
        return this.userRepository.findUserByEmail(email).isPresent();
    }

    public boolean isUserWithUsernamePresent(String username) {
        return this.userRepository.findByUsername(username).isPresent();
    }

    public User findUserByUsername(String username) throws UserNotFoundException {
        return this.userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public ResponseEntity<HttpResponse> addToFavourites(String productIdentifier, Principal principal) throws ProductNotFoundException {
        String username = principal.getName();
        User user = this.userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(USERNAME_NOT_FOUND));

        Optional<Product> optionalProduct = this.productRepository.findProductByIdentifier(productIdentifier);

        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException(PRODUCT_COULD_NOT_BE_FOUND);
        }

        Product product = optionalProduct.get();

        if (!user.getFavouriteProducts().contains(product)) {
            user.getFavouriteProducts().add(product);
        } else {
            HttpResponse response = HttpResponseConstructor.construct(OK,
                    PRODUCT_ALREADY_ADDED_TO_FAVOURITES, NotificationStatus.INFO.name());

            FavouritesHttpResponse favouritesHttpResponse = this.modelMapper.map(response, FavouritesHttpResponse.class);
            favouritesHttpResponse.setFavouriteProducts(loadFavouriteProducts(principal));
            return new ResponseEntity<>(favouritesHttpResponse, HttpStatus.OK);
        }
        HttpResponse response = HttpResponseConstructor.construct(OK,
                String.format(PRODUCT_SUCCESSFULLY_ADDED_TO_FAVOURITES, product.getName()),
                NotificationStatus.SUCCESS.name());

        FavouritesHttpResponse favouritesHttpResponse = this.modelMapper.map(response, FavouritesHttpResponse.class);
        favouritesHttpResponse.setFavouriteProducts(loadFavouriteProducts(principal));
        return new ResponseEntity<>(favouritesHttpResponse, HttpStatus.OK);
    }

    public Set<ProductDto> loadFavouriteProducts(Principal principal) {
        String username = principal.getName();
        return this.userRepository.loadFavouriteProducts(username).stream()
                .map(product -> this.modelMapper.map(product, ProductDto.class))
                .sorted(Comparator.comparing(ProductDto::getCategoryIdentifier).thenComparing(ProductDto::getPrice))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ResponseEntity<HttpResponse> removeFromFavourites(String identifier, Principal principal) throws UserNotFoundException, ProductNotFoundException {
        String username = principal.getName();
        User user = this.userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Product product = productRepository.findProductByIdentifier(identifier).orElseThrow(ProductNotFoundException::new);
        String productName = product.getName();
        boolean isRemoved = user.getFavouriteProducts().remove(product);

        if (!isRemoved) {

            HttpResponse response = HttpResponseConstructor.construct(HttpStatus.BAD_REQUEST,
                    String.format(PRODUCT_NOT_REMOVED_FROM_FAVOURITES, productName),
                    NotificationStatus.ERROR.name());

            FavouritesHttpResponse favouritesHttpResponse = this.modelMapper.map(response, FavouritesHttpResponse.class);
            favouritesHttpResponse.setFavouriteProducts(loadFavouriteProducts(principal));

            return new ResponseEntity<>(favouritesHttpResponse, HttpStatus.BAD_REQUEST);
        }

        HttpResponse httpResponse = HttpResponseConstructor.construct(OK,
                String.format(PRODUCT_REMOVED_FROM_FAVOURITES, productName),
                NotificationStatus.SUCCESS.name());

        FavouritesHttpResponse favouritesHttpResponse = this.modelMapper.map(httpResponse, FavouritesHttpResponse.class);
        favouritesHttpResponse.setFavouriteProducts(loadFavouriteProducts(principal));


        return new ResponseEntity<>(favouritesHttpResponse, HttpStatus.OK);
    }

    public Set<UserDto> findUserByUsernameLike(final String username) {
        return this.userRepository.findUserByUsernameLike(username).stream()
                .map(user -> this.modelMapper.map(user, UserDto.class))
                .collect(Collectors.toSet());
    }
}