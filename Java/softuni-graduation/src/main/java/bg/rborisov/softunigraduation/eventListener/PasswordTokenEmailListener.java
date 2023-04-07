package bg.rborisov.softunigraduation.eventListener;

import bg.rborisov.softunigraduation.dao.PasswordTokenRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.events.PasswordResetEvent;
import bg.rborisov.softunigraduation.exception.AbsentPasswordTokenException;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.PasswordToken;
import bg.rborisov.softunigraduation.model.User;
import bg.rborisov.softunigraduation.util.EmailFactory;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class PasswordTokenEmailListener {
    private static final String EMAIL_CONFIRMATION_SUBJECT = "Password reset email";
    private static final String EMAIL_CONFIRMATION_SENDER = "radii2000@abv.bg";
    private final UserRepository userRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final SpringTemplateEngine springTemplateEngine;
    private final EmailFactory emailFactory;

    public PasswordTokenEmailListener(UserRepository userRepository, PasswordTokenRepository passwordTokenRepository, SpringTemplateEngine springTemplateEngine, EmailFactory emailFactory) {
        this.userRepository = userRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.springTemplateEngine = springTemplateEngine;
        this.emailFactory = emailFactory;
    }

    @Order(2)
    @EventListener
    public void onPasswordResetRequestEvent(PasswordResetEvent event) throws UserNotFoundException, IOException, AbsentPasswordTokenException {
        String email = event.getEmail();
        User user = this.userRepository.findUserByEmail(email).orElseThrow(UserNotFoundException::new);
        PasswordToken passwordToken = this.passwordTokenRepository.findPasswordTokenByUser(user).orElseThrow(AbsentPasswordTokenException::new);

        Map<String, Object> emailParams = new HashMap<>();

        emailParams.put("firstName", user.getFirstName());
        emailParams.put("lastName", user.getLastName());
        emailParams.put("token", passwordToken.getToken());
        Context context = new Context(Locale.ROOT, emailParams);

        Email from = new Email(EMAIL_CONFIRMATION_SENDER);
        Email to = new Email(email);
        String htmlContent = this.springTemplateEngine.process("passwordResetEmail.html", context);

        this.emailFactory.sendEmail(from, to, EMAIL_CONFIRMATION_SUBJECT, htmlContent);
    }
}