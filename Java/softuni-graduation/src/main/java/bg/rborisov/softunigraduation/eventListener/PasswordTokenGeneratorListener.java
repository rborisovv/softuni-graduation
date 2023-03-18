package bg.rborisov.softunigraduation.eventListener;

import bg.rborisov.softunigraduation.dao.PasswordTokenRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.events.PasswordResetEvent;
import bg.rborisov.softunigraduation.exception.AbsentPasswordTokenException;
import bg.rborisov.softunigraduation.exception.PasswordTokenExistsException;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.PasswordToken;
import bg.rborisov.softunigraduation.model.User;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime date = LocalDateTime.now().plusMinutes(30);
        String expireDate = formatter.format(date);

        return new PasswordToken(token, user, expireDate);
    }
}