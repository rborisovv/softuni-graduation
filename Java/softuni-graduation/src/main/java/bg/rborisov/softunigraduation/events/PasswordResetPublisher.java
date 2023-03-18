package bg.rborisov.softunigraduation.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class PasswordResetPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public PasswordResetPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishPasswordResetRequest(String email) {
        PasswordResetEvent passwordResetEvent = new PasswordResetEvent(this, email);
        applicationEventPublisher.publishEvent(passwordResetEvent);
    }
}