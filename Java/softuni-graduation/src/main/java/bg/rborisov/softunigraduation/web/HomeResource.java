package bg.rborisov.softunigraduation.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {

    @GetMapping("/home")
    public String home(HttpServletRequest request, HttpServletResponse response) {
//        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//        response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());

        return "Hello";
    }
}