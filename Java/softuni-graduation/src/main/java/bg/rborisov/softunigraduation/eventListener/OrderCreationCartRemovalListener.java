package bg.rborisov.softunigraduation.eventListener;

import bg.rborisov.softunigraduation.dao.BasketRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.events.OrderCreatedEvent;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.Basket;
import bg.rborisov.softunigraduation.model.Order;
import bg.rborisov.softunigraduation.model.User;
import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@Transactional
public class OrderCreationCartRemovalListener {
    private final UserRepository userRepository;
    private final BasketRepository basketRepository;

    public OrderCreationCartRemovalListener(UserRepository userRepository, BasketRepository basketRepository) {
        this.userRepository = userRepository;
        this.basketRepository = basketRepository;
    }

    @EventListener
    @org.springframework.core.annotation.Order(3)
    public void onApplicationEvent(OrderCreatedEvent event) throws UserNotFoundException {
        Principal principal = event.getPrincipal();
        User user = this.userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);
        Order order = user.getOrder();
        Basket basket = order.getBasket();

        order.setBasket(null);
        user.setBasket(null);
        user.setOrder(null);
        basket.setUser(null);
        this.basketRepository.delete(basket);
    }
}