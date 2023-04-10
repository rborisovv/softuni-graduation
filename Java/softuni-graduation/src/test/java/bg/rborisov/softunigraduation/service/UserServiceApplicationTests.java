package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.ProductRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.UserDto;
import bg.rborisov.softunigraduation.enumeration.NotificationStatus;
import bg.rborisov.softunigraduation.exception.ProductNotFoundException;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.Category;
import bg.rborisov.softunigraduation.model.Media;
import bg.rborisov.softunigraduation.model.Product;
import bg.rborisov.softunigraduation.model.User;
import com.github.javafaker.Faker;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

import static bg.rborisov.softunigraduation.common.Messages.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceApplicationTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @MockBean
    private Principal principal;

    @Mock
    private User user;

    @Mock
    private Product product;

    @Autowired
    private Faker faker;

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
    void testShouldAddToFavourites() throws ProductNotFoundException {
        assertEquals(0, user.getFavouriteProducts().size());

        ResponseEntity<HttpResponse> response = userService.addToFavourites("identifier", this.principal);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(1, user.getFavouriteProducts().size());
    }

    @Test
    void testProductAlreadyAddedToFavourites() throws ProductNotFoundException {
        this.userService.addToFavourites("identifier", this.principal);
        assertEquals(1, this.user.getFavouriteProducts().size());

        ResponseEntity<HttpResponse> response = this.userService.addToFavourites("identifier", this.principal);

        assertEquals(this.user.getFavouriteProducts().size(), 1);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(PRODUCT_ALREADY_ADDED_TO_FAVOURITES, Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void shouldThrowIfNoUserByUsernameFound() {
        Mockito.when(this.userRepository.findByUsername("radi2000")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> this.userService.addToFavourites("radi2000", this.principal));
    }

    @Test
    void shouldThrowIfProductIsNotFoundByIdentifier() {
        Mockito.when(this.productRepository.findProductByIdentifier("identifier")).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> this.userService.addToFavourites("identifier", this.principal));
    }

    @Test
    void shouldRemoveProductFromFavourites() throws ProductNotFoundException, UserNotFoundException {
        this.userService.addToFavourites("identifier", principal);
        assertEquals(1, this.user.getFavouriteProducts().size());

        ResponseEntity<HttpResponse> response = this.userService.removeFromFavourites("identifier", principal);

        assertEquals(0, this.user.getFavouriteProducts().size());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(String.format(PRODUCT_REMOVED_FROM_FAVOURITES, product.getName()),
                Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void shouldNotRemoveProductFromFavouritesWhenItIsNotAlreadyAdded() throws UserNotFoundException, ProductNotFoundException {
        ResponseEntity<HttpResponse> response = this.userService.removeFromFavourites("identifier", this.principal);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertEquals(String.format(PRODUCT_NOT_REMOVED_FROM_FAVOURITES, product.getName()),
                Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(NotificationStatus.ERROR.name().toLowerCase(Locale.ROOT), response.getBody().getNotificationStatus());
    }

    @Test
    void shouldReturnUserByUsernameLike() {
        Mockito.when(this.userRepository.findUserByUsernameLike("radi")).thenReturn(Set.of(user));
        Set<UserDto> users = this.userService.findUserByUsernameLike("radi");

        assertEquals(1, users.size());

        Set<UserDto> emptyMatches = this.userService.findUserByUsernameLike("fakeUsername");

        assertEquals(0, emptyMatches.size());
    }
}