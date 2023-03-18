package bg.rborisov.softunigraduation.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class PasswordResetEvent extends ApplicationEvent {
    private final String email;

    public PasswordResetEvent(Object source, String email) {
        super(source);
        this.email = email;
    }
}