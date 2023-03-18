package bg.rborisov.softunigraduation.eventListener;

import bg.rborisov.softunigraduation.dao.PasswordTokenRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.events.PasswordResetEvent;
import bg.rborisov.softunigraduation.exception.PasswordTokenExistsException;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.PasswordToken;
import bg.rborisov.softunigraduation.model.User;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class PasswordTokenGeneratorListener {
    private final UserRepository userRepository;
    private final PasswordTokenRepository passwordTokenRepository;

    public PasswordTokenGeneratorListener(UserRepository userRepository, PasswordTokenRepository passwordTokenRepository) {
        this.userRepository = userRepository;
        this.passwordTokenRepository = passwordTokenRepository;
    }

    @Order(1)
    @EventListener
    public void onPasswordResetRequestEvent(PasswordResetEvent event) throws UserNotFoundException, PasswordTokenExistsException {
        User user = this.userRepository.findUserByEmail(event.getEmail()).orElseThrow(UserNotFoundException::new);

        if (this.passwordTokenRepository.findPasswordTokenByUser(user).isPresent()) {
            throw new PasswordTokenExistsException();
        }

        PasswordToken passwordToken = createPasswordResetToken(user);
        this.passwordTokenRepository.save(passwordToken);
    }

    private PasswordToken createPasswordResetToken(final User user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expireDate = LocalDateTime.now().plusMinutes(30);

        return new PasswordToken(token, user, expireDate);
    }
}