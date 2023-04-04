package bg.rborisov.softunigraduation.eventListener;

import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.enumeration.LoggerStatus;
import bg.rborisov.softunigraduation.enumeration.OrderStatus;
import bg.rborisov.softunigraduation.events.OrderCreatedEvent;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.User;
import bg.rborisov.softunigraduation.util.logger.OrderLogger;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;

import static bg.rborisov.softunigraduation.common.LogMessages.ORDER_CREATED;

@Component
public class OrderCreationStatusChangerListener {
    private final UserRepository userRepository;

    public OrderCreationStatusChangerListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Async
    @EventListener
    public void onOrderCreationEvent(OrderCreatedEvent event) throws UserNotFoundException, IOException {
        Principal principal = event.getPrincipal();
        User user = this.userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);
        user.getOrder().setOrderStatus(OrderStatus.AWAITING);

        new OrderLogger().log(String.format(ORDER_CREATED, user.getOrder().getOrderNumber()), LoggerStatus.INFO);
    }
}