package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.OrderRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.dto.CheckoutDto;
import bg.rborisov.softunigraduation.dto.OrderDto;
import bg.rborisov.softunigraduation.enumeration.CountryEnum;
import bg.rborisov.softunigraduation.enumeration.OrderStatus;
import bg.rborisov.softunigraduation.events.OrderCreatedPublisher;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.Basket;
import bg.rborisov.softunigraduation.model.Order;
import bg.rborisov.softunigraduation.model.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final OrderCreatedPublisher orderCreatedPublisher;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository, ModelMapper modelMapper, final OrderCreatedPublisher orderCreatedPublisher) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.orderCreatedPublisher = orderCreatedPublisher;
    }

    public boolean canActivateOrderConfirmationFlow(final Principal principal) throws UserNotFoundException {
        User user = this.userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);
        return user.getOrder() != null && user.getOrder().getOrderStatus() == OrderStatus.INITIAL;
    }

    public Set<OrderDto> fetchAllOrders() {
        return this.orderRepository.findAll().stream()
                .map(order -> this.modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void createOrder(Principal principal) {
        this.orderCreatedPublisher.publishOrderCreation(principal);
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
}