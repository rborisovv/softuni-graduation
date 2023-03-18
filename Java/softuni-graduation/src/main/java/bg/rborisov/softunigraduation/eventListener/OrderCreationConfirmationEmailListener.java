package bg.rborisov.softunigraduation.eventListener;

import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.events.OrderCreatedEvent;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.User;
import bg.rborisov.softunigraduation.util.EmailFactory;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class OrderCreationConfirmationEmailListener {
    private static final String EMAIL_CONFIRMATION_SUBJECT = "Order Confirmation Email";
    private static final String EMAIL_CONFIRMATION_SENDER = "radii2000@abv.bg";

    private final UserRepository userRepository;
    private final SpringTemplateEngine springTemplateEngine;
    private final EmailFactory emailFactory;

    public OrderCreationConfirmationEmailListener(UserRepository userRepository, SpringTemplateEngine springTemplateEngine, EmailFactory emailFactory) {
        this.userRepository = userRepository;
        this.springTemplateEngine = springTemplateEngine;
        this.emailFactory = emailFactory;
    }

    @Order(3)
    @EventListener
    public void onOrderCreationEvent(OrderCreatedEvent event) throws IOException, UserNotFoundException {
        Principal principal = event.getPrincipal();
        User user = this.userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);
        bg.rborisov.softunigraduation.model.Order order = user.getOrder();
        String orderEmail = order.getEmail();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime date = LocalDateTime.now();
        String orderDate = formatter.format(date);
        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("firstName", order.getFirstName());
        emailVariables.put("lastName", order.getLastName());
        emailVariables.put("orderNumber", order.getOrderNumber());
        emailVariables.put("orderDate", orderDate);

        Email from = new Email(EMAIL_CONFIRMATION_SENDER);
        Email to = new Email(orderEmail);
        Context context = new Context(Locale.ROOT, emailVariables);
        String htmlContent = this.springTemplateEngine.process("orderConfirmation.html", context);

        this.emailFactory.sendEmail(from, to, EMAIL_CONFIRMATION_SUBJECT, htmlContent);
        //TODO: Log the response info from sg.api(request) call;
    }
}