package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.OrderRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.dto.CheckoutDto;
import bg.rborisov.softunigraduation.enumeration.OrderStatus;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.Basket;
import bg.rborisov.softunigraduation.model.Order;
import bg.rborisov.softunigraduation.model.User;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class OrderServiceApplicationTests {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;
    @MockBean
    private Principal principal;
    private User user;
    private Order order;
    @Autowired
    private Faker faker;

    @BeforeEach
    void init() {
        this.user = new User();
        this.order = new Order();

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

        Basket basket = Basket.builder().creationDate(LocalDateTime.now()).user(user)
                .voucher(null).productMapping(new HashMap<>()).build();

        this.order.setEmail(this.faker.internet().emailAddress());
        this.order.setBasket(basket);
        this.order.setUser(user);
        this.order.setOrderStatus(OrderStatus.INITIAL);
        this.order.setOrderNumber(this.faker.idNumber().valid());
        this.order.setAddress(this.faker.address().fullAddress());
        this.order.setCity(this.faker.address().city());
        this.order.setCity(this.faker.address().country());
        this.order.setFirstName(this.faker.name().firstName());
        this.order.setLastName(this.faker.name().lastName());

        Mockito.when(this.principal.getName()).thenReturn("radi2000");
    }

    @Test
    void shouldCheckIfOrderFlowCanBeActivated() throws UserNotFoundException {
        Mockito.when(this.userRepository.findByUsername("radi2000")).thenReturn(Optional.of(this.user));
        this.user.setOrder(null);
        boolean canActivateOrderFlow = this.orderService.canActivateOrderConfirmationFlow(this.principal);
        assertFalse(canActivateOrderFlow);

        this.user.setOrder(this.order);
        boolean response = this.orderService.canActivateOrderConfirmationFlow(this.principal);
        assertTrue(response);
    }

    @Test
    void shouldSuccessfullyFetchAllOrders() {
        List<Order> orders = List.of(new Order(), new Order(), new Order());
        Mockito.when(this.orderRepository.findAll()).thenReturn(orders);
        assertEquals(3, this.orderService.fetchAllOrders().size());
    }

    @Test
    void shouldReturnEmptyCheckoutData() throws UserNotFoundException {
        this.user.setOrder(null);
        Mockito.when(this.userRepository.findByUsername("radi2000")).thenReturn(Optional.of(this.user));

        ResponseEntity<CheckoutDto> response = this.orderService.fetchCheckoutDataIfPresent(this.principal);

        assertNull(Objects.requireNonNull(response.getBody()).getEmail());
        assertNull(Objects.requireNonNull(response.getBody()).getFirstName());
        assertNull(Objects.requireNonNull(response.getBody()).getLastName());
    }

    @Test
    void shouldReturnPopulatedCheckoutData() throws UserNotFoundException {
        this.user.setOrder(this.order);
        Mockito.when(this.userRepository.findByUsername("radi2000")).thenReturn(Optional.of(this.user));
        ResponseEntity<CheckoutDto> response = this.orderService.fetchCheckoutDataIfPresent(this.principal);

        assertEquals(this.order.getEmail(), Objects.requireNonNull(response.getBody()).getEmail());
        assertEquals(this.order.getFirstName(), Objects.requireNonNull(response.getBody()).getFirstName());
        assertEquals(this.order.getLastName(), Objects.requireNonNull(response.getBody()).getLastName());
    }

    @Test
    void shouldSuccessfullySubmitCheckoutFlow() {
        Mockito.when(this.userRepository.findByUsername("radi2000")).thenReturn(Optional.of(this.user));
        assertDoesNotThrow(() -> this.orderService.submitCheckoutFlow(this.principal, new CheckoutDto()));
    }

    @Test
    void shouldSuccessfullyCreateOrder() {
        this.user.setOrder(this.order);
        Mockito.when(this.userRepository.findByUsername("radi2000")).thenReturn(Optional.of(this.user));
        assertDoesNotThrow(() -> this.orderService.createOrder(this.principal));
    }
}