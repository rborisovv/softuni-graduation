package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.ProductRepository;
import bg.rborisov.softunigraduation.dao.RoleRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.ProductDto;
import bg.rborisov.softunigraduation.dto.UserLoginDto;
import bg.rborisov.softunigraduation.dto.UserRegisterDto;
import bg.rborisov.softunigraduation.dto.UserWelcomeDto;
import bg.rborisov.softunigraduation.enumeration.NotificationStatus;
import bg.rborisov.softunigraduation.enumeration.RoleEnum;
import bg.rborisov.softunigraduation.exception.ProductNotFoundException;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.exception.UserWithUsernameOrEmailExists;
import bg.rborisov.softunigraduation.model.Product;
import bg.rborisov.softunigraduation.model.Role;
import bg.rborisov.softunigraduation.model.User;
import bg.rborisov.softunigraduation.util.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.*;
import static bg.rborisov.softunigraduation.common.JwtConstants.JWT_COOKIE_NAME;
import static bg.rborisov.softunigraduation.common.JwtConstants.TOKEN_PREFIX;
import static bg.rborisov.softunigraduation.common.Messages.*;
import static bg.rborisov.softunigraduation.constant.SecurityConstant.COOKIE_MAX_AGE;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
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

    public UserService(JwtProvider jwtProvider, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, ModelMapper modelMapper,
                       UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, Validator validator, ProductRepository productRepository) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.validator = validator;
        this.productRepository = productRepository;
    }

    public ResponseEntity<UserWelcomeDto> login(UserLoginDto userLoginDto) {
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
        Set<ConstraintViolation<UserRegisterDto>> violations = validator.validate(registerDto);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        preRegisterValidation(registerDto);
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        User user = modelMapper.map(registerDto, User.class);
        user.setUserId(RandomStringUtils.randomAscii(10).replaceAll(" ", ""));
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

    public boolean isAdmin(String authorizationHeader) {
        String token = authorizationHeader.substring(TOKEN_PREFIX.length());
        String username = this.jwtProvider.getSubject(token);

        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_OR_PASSWORD_INCORRECT));

        return user.getRole().getName().equalsIgnoreCase(RoleEnum.ADMIN.name());
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
            HttpResponse httpResponse = HttpResponse.builder()
                    .httpStatusCode(OK.value())
                    .httpStatus(OK)
                    .reason("")
                    .message(PRODUCT_ALREADY_ADDED_TO_FAVOURITES)
                    .notificationStatus(NotificationStatus.INFO.name().toLowerCase(Locale.ROOT))
                    .build();
            return new ResponseEntity<>(httpResponse, HttpStatus.OK);
        }

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setHttpStatus(HttpStatus.OK);
        httpResponse.setHttpStatusCode(HttpStatus.OK.value());
        httpResponse.setReason("");
        httpResponse.setNotificationStatus(NotificationStatus.SUCCESS.name().toLowerCase(Locale.ROOT));
        httpResponse.setMessage(String.format(PRODUCT_SUCCESSFULLY_ADDED_TO_FAVOURITES, product.getName()));

        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
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
        HttpResponse httpResponse = new HttpResponse();

        httpResponse.setHttpStatus(isRemoved ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
        httpResponse.setHttpStatusCode(isRemoved ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value());
        httpResponse.setNotificationStatus(isRemoved ?
                NotificationStatus.SUCCESS.name().toLowerCase(Locale.ROOT) :
                NotificationStatus.ERROR.name().toLowerCase(Locale.ROOT));
        httpResponse.setMessage(isRemoved ?
                String.format(PRODUCT_REMOVED_FROM_FAVOURITES, productName) :
                String.format(PRODUCT_NOT_REMOVED_FROM_FAVOURITES, productName)
        );

        return new ResponseEntity<>(httpResponse, isRemoved ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}