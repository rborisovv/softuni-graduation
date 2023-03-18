package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.*;
import bg.rborisov.softunigraduation.domain.BasketHttpResponse;
import bg.rborisov.softunigraduation.domain.FavouritesHttpResponse;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.*;
import bg.rborisov.softunigraduation.enumeration.*;
import bg.rborisov.softunigraduation.events.OrderCreatedPublisher;
import bg.rborisov.softunigraduation.events.PasswordResetPublisher;
import bg.rborisov.softunigraduation.exception.*;
import bg.rborisov.softunigraduation.model.*;
import bg.rborisov.softunigraduation.util.JwtProvider;
import bg.rborisov.softunigraduation.util.logger.AuthLogger;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
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

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final BasketRepository basketRepository;
    private final OrderRepository orderRepository;
    private final OrderCreatedPublisher orderCreatedPublisher;
    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordResetPublisher passwordResetPublisher;

    public UserService(JwtProvider jwtProvider, UserDetailsService userDetailsService, AuthenticationManager authenticationManager,
                       ModelMapper modelMapper, UserRepository userRepository, PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository, Validator validator, ProductRepository productRepository,
                       BasketRepository basketRepository, OrderRepository orderRepository, OrderCreatedPublisher orderCreatedPublisher, PasswordTokenRepository passwordTokenRepository, PasswordResetPublisher passwordResetPublisher) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.validator = validator;
        this.productRepository = productRepository;
        this.basketRepository = basketRepository;
        this.orderRepository = orderRepository;
        this.orderCreatedPublisher = orderCreatedPublisher;
        this.passwordTokenRepository = passwordTokenRepository;
        this.passwordResetPublisher = passwordResetPublisher;
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


        UserWelcomeDto userWelcomeDto = modelMapper.map(user, UserWelcomeDto.class);
        AuthLogger authLogger = new AuthLogger();
        authLogger.log(String.format("User %s successfully logged in!", user.getUsername()), LoggerStatus.INFO);
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
            HttpResponse response = constructHttpResponse(OK,
                    PRODUCT_ALREADY_ADDED_TO_FAVOURITES, NotificationStatus.INFO.name());

            FavouritesHttpResponse favouritesHttpResponse = this.modelMapper.map(response, FavouritesHttpResponse.class);
            favouritesHttpResponse.setFavouriteProducts(loadFavouriteProducts(principal));
            return new ResponseEntity<>(favouritesHttpResponse, HttpStatus.OK);
        }
        HttpResponse response = constructHttpResponse(OK, String.format(PRODUCT_SUCCESSFULLY_ADDED_TO_FAVOURITES, product.getName()),
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

            HttpResponse response = constructHttpResponse(HttpStatus.BAD_REQUEST,
                    String.format(PRODUCT_NOT_REMOVED_FROM_FAVOURITES, productName),
                    NotificationStatus.ERROR.name());

            FavouritesHttpResponse favouritesHttpResponse = this.modelMapper.map(response, FavouritesHttpResponse.class);
            favouritesHttpResponse.setFavouriteProducts(loadFavouriteProducts(principal));

            return new ResponseEntity<>(favouritesHttpResponse, HttpStatus.BAD_REQUEST);
        }

        HttpResponse httpResponse = constructHttpResponse(OK,
                String.format(PRODUCT_REMOVED_FROM_FAVOURITES, productName),
                NotificationStatus.SUCCESS.name());

        FavouritesHttpResponse favouritesHttpResponse = this.modelMapper.map(httpResponse, FavouritesHttpResponse.class);
        favouritesHttpResponse.setFavouriteProducts(loadFavouriteProducts(principal));


        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }

    private HttpResponse constructHttpResponse(HttpStatus httpStatus, String message, String notificationStatus) {
        HttpResponse response = new HttpResponse();
        response.setHttpStatus(httpStatus);
        response.setHttpStatusCode(httpStatus.value());
        response.setNotificationStatus(notificationStatus.toLowerCase(Locale.ROOT));
        response.setReason("");
        response.setMessage(message);

        return response;
    }

    public ResponseEntity<HttpResponse> addToBasket(final String identifier, final Principal principal) throws ProductNotFoundException, UserNotFoundException {
        //TODO: Find a way to add quantity to ordered Products
        if (identifier.isBlank()) {
            throw new IllegalArgumentException();
        }

        User user = this.userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);
        Product product = this.productRepository.findProductByIdentifier(identifier).orElseThrow(ProductNotFoundException::new);

        if (user.getBasket() != null && user.getBasket().getProductMapping().containsKey(product)) {
            HttpResponse httpResponse = constructHttpResponse(
                    OK, String.format(PRODUCT_ALREADY_ADDED_TO_BASKET, product.getName()),
                    NotificationStatus.INFO.name().toLowerCase(Locale.ROOT));

            BasketHttpResponse basketHttpResponse = this.modelMapper.map(httpResponse, BasketHttpResponse.class);
            basketHttpResponse.setBasketProducts(loadBasket(principal));
            return new ResponseEntity<>(basketHttpResponse, HttpStatus.OK);
        }

        Basket basket = user.getBasket();

        if (basket == null) {
            basket = new Basket();
            basket.setProductMapping(new HashMap<>());
        }

        basket.getProductMapping().put(product, 1);
        this.basketRepository.save(basket);
        user.setBasket(basket);

        HttpResponse httpResponse = constructHttpResponse(
                OK, String.format(PRODUCT_SUCCESSFULLY_ADDED_TO_BASKET, product.getName()),
                NotificationStatus.SUCCESS.name().toLowerCase(Locale.ROOT)
        );

        BasketHttpResponse basketHttpResponse = this.modelMapper.map(httpResponse, BasketHttpResponse.class);
        basketHttpResponse.setBasketProducts(loadBasket(principal));
        return new ResponseEntity<>(basketHttpResponse, HttpStatus.OK);
    }

    public Set<ProductDto> loadBasket(Principal principal) throws UserNotFoundException {
        User user = this.userRepository.findByUsername(principal.getName())
                .orElseThrow(UserNotFoundException::new);

        return loadSortedUserBasketProducts(user);
    }

    private Set<ProductDto> loadSortedUserBasketProducts(User user) {
        if (user.getBasket() == null || user.getBasket().getProductMapping().isEmpty()) {
            return Collections.emptySet();
        }

        return user.getBasket().getProductMapping().entrySet()
                .stream().map((entry) -> {
                    ProductDto productDto = this.modelMapper.map(entry.getKey(), ProductDto.class);
                    productDto.setQuantity(entry.getValue());
                    return productDto;
                })
                .sorted(Comparator.comparing(ProductDto::getCategoryIdentifier).thenComparing(ProductDto::getPrice))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ResponseEntity<HttpResponse> removeFromBasket(final String identifier, final Principal principal) throws ProductNotFoundException, UserNotFoundException {
        String username = principal.getName();

        Product product = this.productRepository.findProductByIdentifier(identifier).orElseThrow(ProductNotFoundException::new);
        User user = this.userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        Basket basket = user.getBasket();
        basket.getProductMapping().remove(product);

        if (basket.getProductMapping() == null || basket.getProductMapping().isEmpty()) {
            user.setBasket(null);
            this.basketRepository.delete(basket);
        }

        HttpResponse httpResponse = constructHttpResponse(HttpStatus.OK,
                String.format(PRODUCT_REMOVED_FROM_BASKET, product.getName()), NotificationStatus.SUCCESS.name());
        BasketHttpResponse response = this.modelMapper.map(httpResponse, BasketHttpResponse.class);

        Set<ProductDto> userBasketProducts = loadSortedUserBasketProducts(user);
        response.setBasketProducts(userBasketProducts);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<HttpResponse> updateBasketProduct(Principal principal, Map<String, String> productParams) throws ProductNotFoundException, UserNotFoundException, BasketNotFoundException {
        String identifier = productParams.get("identifier");
        Integer quantity = Integer.parseInt(productParams.get("quantity"));
        Product product = this.productRepository.findProductByIdentifier(identifier).orElseThrow(ProductNotFoundException::new);
        User user = this.userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);
        Basket basket = this.basketRepository.findBasketByUser(user).orElseThrow(BasketNotFoundException::new);

        basket.getProductMapping().put(product, quantity);
        this.basketRepository.save(basket);

        Set<ProductDto> basketProducts = loadSortedUserBasketProducts(user);
        BasketHttpResponse basketHttpResponse = new BasketHttpResponse();
        basketHttpResponse.setHttpStatus(HttpStatus.OK);
        basketHttpResponse.setHttpStatusCode(HttpStatus.OK.value());
        basketHttpResponse.setBasketProducts(basketProducts);

        return new ResponseEntity<>(basketHttpResponse, HttpStatus.OK);
    }

    public void submitCheckoutFlow(Principal principal, CheckoutDto checkoutDto) throws UserNotFoundException {
        User user = this.userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);
        Order order = user.getOrder() == null ? this.modelMapper.map(checkoutDto, Order.class) : user.getOrder();
        Basket basket = user.getBasket();
        this.modelMapper.map(checkoutDto, order);
        String orderNumber = RandomStringUtils.randomNumeric(12);
        order.setOrderNumber(orderNumber);
        order.setOrderStatus(OrderStatus.INITIAL);
        order.setCountry(CountryEnum.BULGARIA.name());
        order.setBasket(basket);
        order.setUser(user);
        user.setOrder(order);
        user.setOrder(order);

        this.orderRepository.save(order);
        this.userRepository.save(user);
    }

    public ResponseEntity<CheckoutDto> fetchCheckoutDataIfPresent(Principal principal) throws UserNotFoundException {
        User user = this.userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);
        Order order = user.getOrder();
        if (user.getOrder() == null) {
            return new ResponseEntity<>(new CheckoutDto(), HttpStatus.OK);
        }
        CheckoutDto checkoutDto = this.modelMapper.map(order, CheckoutDto.class);
        return new ResponseEntity<>(checkoutDto, HttpStatus.OK);
    }

    public void createOrder(Principal principal) {
        this.orderCreatedPublisher.publishOrderCreation(principal);
    }

    public ResponseEntity<HttpResponse> resetPassword(final String email) {
        this.passwordResetPublisher.publishPasswordResetRequest(email);
        HttpResponse httpResponse = constructHttpResponse(HttpStatus.OK, String.format(PASSWORD_RESET_EMAIL, email),
                NotificationStatus.SUCCESS.name());

        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }

    public ResponseEntity<HttpResponse> changePassword(final PasswordChangeDto passwordChangeDto) throws AbsentPasswordTokenException, PasswordTokenExpiredException {
        preActivePasswordResetRequestCheck(passwordChangeDto.getToken());

        PasswordToken passwordToken = this.passwordTokenRepository.findByToken(passwordChangeDto.getToken()).orElseThrow(AbsentPasswordTokenException::new);
        User user = passwordToken.getUser();
        user.setPassword(this.passwordEncoder.encode(passwordChangeDto.getPassword()));

        HttpResponse httpResponse = constructHttpResponse(HttpStatus.OK, PASSWORD_CHANGED_SUCCESSFULLY, NotificationStatus.SUCCESS.name());
        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }

    public void preActivePasswordResetRequestCheck(String token) throws AbsentPasswordTokenException, PasswordTokenExpiredException {
        Optional<PasswordToken> optionalPasswordToken = this.passwordTokenRepository.findByToken(token);

        if (optionalPasswordToken.isEmpty()) {
            throw new AbsentPasswordTokenException();
        }
        LocalDateTime expireDate = optionalPasswordToken.get().getExpireDate();

        if (expireDate.isBefore(LocalDateTime.now())) {
            throw new PasswordTokenExpiredException();
        }
    }

    public Boolean hasActivePasswordRequest(String token) {
        Optional<PasswordToken> passwordToken = this.passwordTokenRepository.findByToken(token);

        if (passwordToken.isEmpty()) {
            return false;
        }

        LocalDateTime expireDate = passwordToken.get().getExpireDate();
        return expireDate.isAfter(LocalDateTime.now());
    }
}