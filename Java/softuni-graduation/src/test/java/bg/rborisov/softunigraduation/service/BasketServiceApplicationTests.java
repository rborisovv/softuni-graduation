package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.BasketRepository;
import bg.rborisov.softunigraduation.dao.ProductRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.dao.VoucherRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.VoucherDto;
import bg.rborisov.softunigraduation.enumeration.VoucherTypeEnum;
import bg.rborisov.softunigraduation.exception.*;
import bg.rborisov.softunigraduation.model.*;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static bg.rborisov.softunigraduation.common.Messages.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BasketServiceApplicationTests {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private BasketRepository basketRepository;
    @MockBean
    private Principal principal;
    @MockBean
    private VoucherRepository voucherRepository;
    @Autowired
    private BasketService basketService;
    @Autowired
    private Faker faker;
    private User user;
    private Product product;

    @BeforeEach
    void init() {
        this.user = new User();
        this.product = new Product();

        this.user.setUserId(faker.idNumber().valid());
        this.user.setUsername("radi2000");
        this.user.setIsLocked(false);
        this.user.setRole(null);
        this.user.setJoinDate(LocalDate.now());
        this.user.setBasket(null);
        this.user.setOrder(null);
        this.user.setBirthDate(LocalDate.now());
        this.user.setEmail(faker.internet().emailAddress());
        this.user.setFavouriteProducts(new HashSet<>());
        this.user.setFirstName(faker.name().firstName());
        this.user.setLastName(faker.name().lastName());
        this.user.setImageUrl(faker.internet().image());
        this.user.setPassword(faker.internet().password());
        this.user.setVouchers(new HashSet<>());

        this.product.setBestBefore(LocalDate.now());
        this.product.setCategory(new Category());
        this.product.setName(faker.commerce().productName());
        this.product.setIdentifier(faker.idNumber().valid());
        this.product.setMedia(new Media());
        this.product.setCreationTime(LocalDate.now());
        this.product.setDescription(null);
        this.product.setPrice(BigDecimal.TEN);
        this.product.setShowBuyButton(true);
        this.product.setStockLevel(faker.number().numberBetween(1, 10));

        Mockito.when(userRepository.findByUsername("radi2000")).thenReturn(Optional.of(user));
        Mockito.when(productRepository.findProductByIdentifier("identifier")).thenReturn(Optional.of(product));
        Mockito.when(principal.getName()).thenReturn("radi2000");
    }


    @Test
    void shouldTestIfUserCanActivateCheckout() throws UserNotFoundException {
        Mockito.when(this.userRepository.findByUsername("user")).thenReturn(Optional.of(this.user));
        Mockito.when(this.basketRepository.findBasketByUser(this.user)).thenReturn(Optional.of(new Basket()));
        Mockito.when(this.principal.getName()).thenReturn("user");

        boolean canActivateCheckout = this.basketService.canActivateCheckout(this.principal);
        assertTrue(canActivateCheckout);

        Mockito.when(this.basketRepository.findBasketByUser(this.user)).thenReturn(Optional.empty());
        boolean response = this.basketService.canActivateCheckout(this.principal);
        assertFalse(response);
    }

    @Test
    void testShouldFailWhenNoProductIdentifierIsPassed() {
        String productIdentifier = "";
        assertThrows(IllegalArgumentException.class,
                () -> this.basketService.addToBasket(productIdentifier, this.principal));
    }

    @Test
    void testShouldFailIfProductIsSoldOut() {
        this.product.setStockLevel(0);
        assertThrows(ProductSoldOutException.class, () -> this.basketService.addToBasket("identifier", this.principal));
    }

    @Test
    void shouldTestIfProductIsAlreadyAddedToBasket() throws UserNotFoundException, ProductSoldOutException, ProductNotFoundException {
        Map<Product, Integer> map = new HashMap<>();
        map.put(this.product, 1);

        Basket basket = Basket.builder().creationDate(LocalDateTime.now()).user(user).voucher(null).productMapping(map).build();
        this.user.setBasket(basket);

        ResponseEntity<HttpResponse> response = this.basketService.addToBasket("identifier", this.principal);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(String.format(PRODUCT_ALREADY_ADDED_TO_BASKET, product.getName()),
                Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void shouldSuccessfullyAddToBasket() throws UserNotFoundException, ProductSoldOutException, ProductNotFoundException {
        this.user.setBasket(null);
        ResponseEntity<HttpResponse> response = this.basketService.addToBasket("identifier", this.principal);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(String.format(PRODUCT_SUCCESSFULLY_ADDED_TO_BASKET, this.product.getName()),
                Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void shouldThrowWhenInvalidProductIdentifierPassed() {
        Map<String, String> map = new HashMap<>();
        map.put("identifier", "identifier");
        map.put("quantity", "0");

        Mockito.when(this.productRepository.findProductByIdentifier("identifier")).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class,
                () -> this.basketService.updateBasketProduct(this.principal, map));
    }

    @Test
    void shouldThrowWhenNoUserFound() {
        Map<String, String> map = new HashMap<>();
        map.put("identifier", "identifier");
        map.put("quantity", "0");

        Mockito.when(this.userRepository.findByUsername("radi2000")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> this.basketService.updateBasketProduct(this.principal, map));
    }

    @Test
    void shouldThrowWhenNoBasketPresent() {
        Map<String, String> map = new HashMap<>();
        map.put("identifier", "identifier");
        map.put("quantity", "0");

        Mockito.when(this.userRepository.findByUsername("radi2000")).thenReturn(Optional.of(this.user));
        this.user.setBasket(null);
        assertThrows(BasketNotFoundException.class,
                () -> this.basketService.updateBasketProduct(this.principal, map));
    }

    @Test
    void shouldSuccessfullyUpdateProduct() throws UserNotFoundException, BasketNotFoundException, ProductNotFoundException {
        Map<Product, Integer> productsMap = new HashMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("identifier", "identifier");
        map.put("quantity", "1");

        Basket basket = Basket.builder().creationDate(LocalDateTime.now()).user(user).voucher(null).productMapping(productsMap).build();
        this.user.setBasket(basket);
        Mockito.when(this.basketRepository.findBasketByUser(user)).thenReturn(Optional.of(this.user.getBasket()));

        ResponseEntity<HttpResponse> response = this.basketService.updateBasketProduct(this.principal, map);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    }

    @Test
    void shouldSuccessfullyRemoveProductFromBasket() throws UserNotFoundException, ProductNotFoundException {
        Basket basket = Basket.builder().creationDate(LocalDateTime.now()).user(user).voucher(null).productMapping(new HashMap<>()).build();
        this.user.setBasket(basket);

        ResponseEntity<HttpResponse> response = this.basketService.removeFromBasket("identifier", this.principal);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(String.format(PRODUCT_REMOVED_FROM_BASKET, this.product.getName()),
                Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void shouldFailIfVoucherIsAlreadyAdded() {
        Voucher voucher = mockForSuccessfulVoucherInsertion();
        this.user.getBasket().setVoucher(voucher);

        Mockito.when(this.voucherRepository.findVoucherByName("name")).thenReturn(Optional.of(voucher));
        assertThrows(OneVoucherAlreadyAddedException.class, () -> this.basketService.addVoucherToBasket("name"));
    }

    @Test
    void shouldFailIfNoVoucherFound() {
        mockForSuccessfulVoucherInsertion();
        Mockito.when(this.voucherRepository.findVoucherByName("name")).thenReturn(Optional.empty());
        assertThrows(AbsentVoucherByNameException.class, () -> this.basketService.addVoucherToBasket("name"));
    }

    @Test
    void shouldFailIfVoucherIsExpired() {
        Voucher voucher = mockForSuccessfulVoucherInsertion();
        voucher.setExpirationDate(LocalDate.now().minusDays(5));
        assertThrows(VoucherExpiredException.class, () -> this.basketService.addVoucherToBasket("Promo10"));
    }

    @Test
    void shouldFailIfVoucherIsNotBoundToCurrentUser() {
        Voucher voucher = mockForSuccessfulVoucherInsertion();
        User fakeUser = new User();
        fakeUser.setUsername("fakeUsername");
        voucher.setUser(fakeUser);

        assertThrows(VoucherCannotBeUsedByUserException.class,
                () -> this.basketService.addVoucherToBasket("Promo10"));
    }

    @Test
    void shouldFailIfNoBasketFoundForUser() {
        mockForSuccessfulVoucherInsertion();
        this.user.setBasket(null);
        assertThrows(UserHasNoBasketException.class, () -> this.basketService.addVoucherToBasket("Promo10"));
    }

    @Test
    void shouldSuccessfullyAddVoucherToBasket() throws Exception {
        mockForSuccessfulVoucherInsertion();

        ResponseEntity<VoucherDto> response = this.basketService.addVoucherToBasket("Promo10");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals("Promo10", Objects.requireNonNull(response.getBody()).getName());
        assertEquals(VoucherTypeEnum.RELATIVE.name(), response.getBody().getType());
        assertEquals(BigDecimal.TEN, response.getBody().getDiscount());
        assertEquals(LocalDate.now().plusDays(7), response.getBody().getExpirationDate());
    }

    private Voucher mockForSuccessfulVoucherInsertion() {
        Voucher voucher = new Voucher();
        voucher.setCreationDate(LocalDate.now());
        voucher.setName("Promo10");
        voucher.setCategory(null);
        voucher.setUser(this.user);
        voucher.setType(VoucherTypeEnum.RELATIVE);
        voucher.setDiscount(BigDecimal.TEN);
        voucher.setExpirationDate(LocalDate.now().plusDays(7));

        Basket basket = Basket.builder().creationDate(LocalDateTime.now()).user(user).voucher(null).productMapping(new HashMap<>()).build();
        this.user.setBasket(basket);

        Mockito.when(this.voucherRepository.findVoucherByName("Promo10")).thenReturn(Optional.of(voucher));

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getName()).thenReturn("radi2000");
        SecurityContextHolder.setContext(securityContext);

        return voucher;
    }

    @Test
    void shouldSuccessfullyReturnVoucherIfPresentInBasket() throws UserNotFoundException {
        VoucherDto nullResponse = this.basketService.fetchVoucherIfPresent(this.principal);
        assertNull(nullResponse);

        Voucher voucher = new Voucher();
        voucher.setCreationDate(LocalDate.now());
        voucher.setName("Promo10");
        voucher.setCategory(null);
        voucher.setUser(this.user);
        voucher.setType(VoucherTypeEnum.RELATIVE);
        voucher.setDiscount(BigDecimal.TEN);
        voucher.setExpirationDate(LocalDate.now().plusDays(7));

        Basket basket = Basket.builder().creationDate(LocalDateTime.now()).user(user).voucher(voucher).productMapping(new HashMap<>()).build();
        this.user.setBasket(basket);

        VoucherDto voucherDto = this.basketService.fetchVoucherIfPresent(this.principal);

        assertEquals("Promo10", voucherDto.getName());
        assertEquals(BigDecimal.TEN, voucherDto.getDiscount());
        assertEquals(VoucherTypeEnum.RELATIVE.name(), voucherDto.getType());
        assertEquals(LocalDate.now().plusDays(7), voucherDto.getExpirationDate());
    }
}