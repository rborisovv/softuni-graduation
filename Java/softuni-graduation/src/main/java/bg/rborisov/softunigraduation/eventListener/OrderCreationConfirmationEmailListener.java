package bg.rborisov.softunigraduation.eventListener;

import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.events.OrderCreatedEvent;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.User;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;

@Component
public class OrderCreationConfirmationEmailListener {
    private static final String EMAIL_CONFIRMATION_SUBJECT = "Order Confirmation Email";
    private static final String EMAIL_CONFIRMATION_SENDER = "radii2000@abv.bg";

    private final UserRepository userRepository;

    @Value("${sendgrid.apiKey}")
    private String sendGridApiKey;

    public OrderCreationConfirmationEmailListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Order(3)
    @EventListener
    public void onApplicationEvent(OrderCreatedEvent event) throws IOException, UserNotFoundException {
        Principal principal = event.getPrincipal();
        User user = this.userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);
        String orderEmail = user.getOrder().getEmail();

        Email from = new Email(EMAIL_CONFIRMATION_SENDER);
        Email to = new Email(orderEmail);
        Content content = new Content("text/plain", "Your order has been successfully received!"
                + System.lineSeparator() + "Thank you for choosing us!");
        Mail mail = new Mail(from, EMAIL_CONFIRMATION_SUBJECT, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        sg.api(request);

        //TODO: Log the response info from sg.api(request) call;
    }
}