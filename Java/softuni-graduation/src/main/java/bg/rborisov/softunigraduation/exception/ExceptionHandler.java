package bg.rborisov.softunigraduation.exception;

import bg.rborisov.softunigraduation.domain.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.ACCOUNT_LOCKED;
import static bg.rborisov.softunigraduation.common.ExceptionMessages.USER_WITH_USERNAME_OR_EMAIL_EXISTS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> accountLockedException() {
        return createHttpResponse(BAD_REQUEST, ACCOUNT_LOCKED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserWithUsernameOrEmailExists.class)
    public ResponseEntity<HttpResponse> usernameOrEmailExists() {
        return createHttpResponse(BAD_REQUEST, USER_WITH_USERNAME_OR_EMAIL_EXISTS);
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        HttpResponse httpResponse = new HttpResponse(httpStatus.value(),
                httpStatus, httpStatus.getReasonPhrase().toUpperCase(Locale.ROOT), message);
        log.error(message);
        return new ResponseEntity<>(httpResponse, httpStatus);
    }
}