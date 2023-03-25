package bg.rborisov.softunigraduation.eventListener;

import bg.rborisov.softunigraduation.service.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuthenticationFailureListener {
    private final LoginAttemptService loginAttemptService;

    public AuthenticationFailureListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    @SuppressWarnings("unused")
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        loginAttemptService.addUserToLoginAttemptCache(request);
    }
}
