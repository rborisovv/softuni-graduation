package bg.rborisov.softunigraduation.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.security.Principal;


@Getter
@Setter
public class OrderCreatedEvent extends ApplicationEvent {
    private Principal principal;

    public OrderCreatedEvent(Object source, Principal principal) {
        super(source);
        this.principal = principal;
    }
}