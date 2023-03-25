package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.util.LoginCacheModel;
import bg.rborisov.softunigraduation.util.validators.ClientIpValidator;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class LoginAttemptService {
    private static final int ATTEMPT_INCREMENT = 1;
    private static final int MAXIMUM_NUMBER_OF_LOGIN_ATTEMPTS = 5;
    private final LoadingCache<String, LoginCacheModel<Integer>> loginAttemptCache;

    public LoginAttemptService(LoadingCache<String, LoginCacheModel<Integer>> loginAttemptCache) {
        this.loginAttemptCache = loginAttemptCache;
    }

    public void evictUserFromLoginAttemptCache(final HttpServletRequest request) {
        String clientIpAddress = ClientIpValidator.validateIpAddress(request);
        loginAttemptCache.invalidate(clientIpAddress);
    }

    public void addUserToLoginAttemptCache(final HttpServletRequest request) {
        String clientIpAddress = ClientIpValidator.validateIpAddress(request);
        try {
            Integer attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(clientIpAddress).getValue();
            loginAttemptCache.put(clientIpAddress, new LoginCacheModel<>(attempts));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasExceededMaxAttempts(final HttpServletRequest request) {
        try {
            String clientIpAddress = ClientIpValidator.validateIpAddress(request);
            return loginAttemptCache.get(clientIpAddress).getValue() >= MAXIMUM_NUMBER_OF_LOGIN_ATTEMPTS;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }
}