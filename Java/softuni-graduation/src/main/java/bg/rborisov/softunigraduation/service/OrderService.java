package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.OrderRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.dto.OrderDto;
import bg.rborisov.softunigraduation.enumeration.OrderStatus;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    public OrderService(UserRepository userRepository, OrderRepository orderRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
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
}