package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.enumeration.OrderStatus;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Slf4j
@Service
@Transactional
public class OrderService {
    private final UserRepository userRepository;

    public OrderService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean canActivateOrderConfirmationFlow(final Principal principal) throws UserNotFoundException {
        User user = this.userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);
        return user.getOrder() != null && user.getOrder().getOrderStatus() == OrderStatus.INITIAL;
    }
}