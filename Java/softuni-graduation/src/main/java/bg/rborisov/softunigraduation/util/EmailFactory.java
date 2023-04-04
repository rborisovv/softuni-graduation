package bg.rborisov.softunigraduation.util;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public final class EmailFactory {

    @Value("${sendgrid.apiKey}")
    private String sendGridApiKey;

    public void sendEmail(Email from, Email to, String emailConfirmationSubject, String htmlContent) throws IOException {
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, emailConfirmationSubject, to, content);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        final Response apiResponse = sg.api(request);

        final String msgResponseBuilder = apiResponse.getStatusCode() +
                System.lineSeparator() + apiResponse.getHeaders();

        log.info(msgResponseBuilder.trim());
    }
}