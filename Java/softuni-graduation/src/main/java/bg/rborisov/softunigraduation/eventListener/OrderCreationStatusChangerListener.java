package bg.rborisov.softunigraduation.eventListener;

import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.enumeration.OrderStatus;
import bg.rborisov.softunigraduation.events.OrderCreatedEvent;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.User;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class OrderCreationStatusChangerListener {
    private final UserRepository userRepository;

    public OrderCreationStatusChangerListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Order(1)
    @EventListener
    public void onApplicationEvent(OrderCreatedEvent event) throws UserNotFoundException {
        Principal principal = event.getPrincipal();
        User user = this.userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);
        user.getOrder().setOrderStatus(OrderStatus.COMPLETED);
    }
}