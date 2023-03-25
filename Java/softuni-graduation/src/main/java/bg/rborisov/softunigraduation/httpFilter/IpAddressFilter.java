package bg.rborisov.softunigraduation.httpFilter;

import bg.rborisov.softunigraduation.util.validators.ClientIpValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

import static bg.rborisov.softunigraduation.constant.SecurityConstant.AUTH_ENDPOINT;

@Component
public class IpAddressFilter extends OncePerRequestFilter {
    private static final String WHITE_LISTED_IP = "127.0.0.1";

    @Override
    protected void doFilterInternal(@NonNull  HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        if (!requestWrapper.getRequestURI().equals(AUTH_ENDPOINT)) {
            filterChain.doFilter(requestWrapper, response);
            return;
        }

        String clientIpAddress = ClientIpValidator.validateIpAddress(requestWrapper);

        if (StringUtils.equals(WHITE_LISTED_IP, clientIpAddress)) {
            filterChain.doFilter(requestWrapper, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}