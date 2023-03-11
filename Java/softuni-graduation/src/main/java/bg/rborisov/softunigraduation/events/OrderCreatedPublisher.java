package bg.rborisov.softunigraduation.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class OrderCreatedPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderCreatedPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishOrderCreation(final Principal principal) {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(this, principal);
        applicationEventPublisher.publishEvent(orderCreatedEvent);
    }
}
