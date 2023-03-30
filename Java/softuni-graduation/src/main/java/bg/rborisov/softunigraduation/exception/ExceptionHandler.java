package bg.rborisov.softunigraduation.exception;

import bg.rborisov.softunigraduation.common.ExceptionMessages;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.util.LoginCacheModel;
import bg.rborisov.softunigraduation.util.validators.ClientIpValidator;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Duration;
import java.time.LocalDateTime;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ExceptionHandler extends AbstractExceptionHandler {
    private final LoadingCache<String, LoginCacheModel<Integer>> loadingCache;

    public ExceptionHandler(LoadingCache<String, LoginCacheModel<Integer>> loadingCache) {
        this.loadingCache = loadingCache;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> accountLockedException(final HttpServletRequest request) {
        String clientIpAddress = ClientIpValidator.validateIpAddress(request);
        LoginCacheModel<Integer> cacheModel = this.loadingCache.getIfPresent(clientIpAddress);
        Duration lockTimeRemaining = Duration.ZERO;

        if (cacheModel != null) {
            LocalDateTime lockDurationExpireTime = cacheModel.getTimestamp();
            LocalDateTime currentTime = LocalDateTime.now();
            lockTimeRemaining = Duration.between(currentTime, lockDurationExpireTime);

        }

        return super.createHttpResponse(BAD_REQUEST, String.format(ACCOUNT_LOCKED,
                lockTimeRemaining.toMinutes()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> BadCredentials() {
        return super.createHttpResponse(BAD_REQUEST, USERNAME_OR_PASSWORD_INCORRECT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserWithUsernameOrEmailExists.class)
    public ResponseEntity<HttpResponse> usernameOrEmailExists() {
        return super.createHttpResponse(BAD_REQUEST, USER_WITH_USERNAME_OR_EMAIL_EXISTS);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<HttpResponse> categoryNotFound() {
        return super.createHttpResponse(BAD_REQUEST, CATEGORY_NOT_FOUND);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(CategoryWithIdentifierExists.class)
    public ResponseEntity<HttpResponse> categoryByIdentifierExists() {
        return super.createHttpResponse(BAD_REQUEST, ExceptionMessages.CATEGORY_BY_IDENTIFIER_EXISTS);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CategoryWithNameExists.class)
    public ResponseEntity<HttpResponse> categoryByNameExists() {
        return super.createHttpResponse(BAD_REQUEST, CATEGORY_BY_NAME_EXISTS);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MediaByNameAlreadyExistsException.class)
    public ResponseEntity<HttpResponse> mediaByNameAlreadyExists() {
        return super.createHttpResponse(BAD_REQUEST, MEDIA_BY_NAME_ALREADY_EXISTS);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<HttpResponse> objectContainsDbRelations() {
        return super.createHttpResponse(BAD_REQUEST, OBJECT_CONTAINS_EXTERNAL_RELATIONS);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AbsentPasswordTokenException.class)
    public ResponseEntity<HttpResponse> passwordTokenNotFound() {
        return super.createHttpResponse(BAD_REQUEST, PASSWORD_TOKEN_NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(PasswordTokenExistsException.class)
    public ResponseEntity<HttpResponse> passwordTokenAlreadyGenerated() {
        return super.createHttpResponse(BAD_REQUEST, PASSWORD_TOKEN_EXISTS);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(PasswordTokenExpiredException.class)
    public ResponseEntity<HttpResponse> passwordTokenExpired() {
        return super.createHttpResponse(BAD_REQUEST, PASSWORD_TOKEN_EXPIRED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(VoucherByNameAlreadyPresent.class)
    public ResponseEntity<HttpResponse> voucherByNamePresent() {
        return super.createHttpResponse(BAD_REQUEST, VOUCHER_BY_NAME_PRESENT);
    }
}