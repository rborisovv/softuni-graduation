package bg.rborisov.softunigraduation.httpFilter;

import bg.rborisov.softunigraduation.util.validators.ClientIpValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static bg.rborisov.softunigraduation.constant.SecurityConstant.AUTH_ENDPOINT;

@Component
public class IpAddressFilter extends OncePerRequestFilter {
    private static final String WHITE_LISTED_IP = "127.0.0.1";

    @Override
    @SuppressWarnings("all")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().equals(AUTH_ENDPOINT)) {
            filterChain.doFilter(request, response);
        }

        String clientIpAddress = ClientIpValidator.validateIpAddress(request);

        if (StringUtils.equals(WHITE_LISTED_IP, clientIpAddress)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}