package bg.rborisov.softunigraduation.eventListener;

import bg.rborisov.softunigraduation.dao.ProductRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.events.OrderCreatedEvent;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.Product;
import bg.rborisov.softunigraduation.model.User;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.stream.Collectors;

@Component
public class OrderCreationUpdateStockLvlEventListener {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderCreationUpdateStockLvlEventListener(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Order(1)
    @EventListener
    public void onOrderCreationEvent(OrderCreatedEvent event) throws UserNotFoundException {
        Principal principal = event.getPrincipal();
        User user = this.userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);
        this.productRepository.saveAll(user.getOrder().getBasket()
                .getProductMapping()
                .entrySet()
                .stream()
                .map(entry -> {
                    Product product = entry.getKey();
                    product.setStockLevel(product.getStockLevel() - entry.getValue());
                    return product;
                })
                .collect(Collectors.toSet()));
    }
}