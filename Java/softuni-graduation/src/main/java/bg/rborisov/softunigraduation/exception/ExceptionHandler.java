package bg.rborisov.softunigraduation.exception;

import bg.rborisov.softunigraduation.common.ExceptionMessages;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> accountLockedException() {
        return createHttpResponse(BAD_REQUEST, ACCOUNT_LOCKED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> BadCredentials() {
        return createHttpResponse(BAD_REQUEST, USERNAME_OR_PASSWORD_INCORRECT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserWithUsernameOrEmailExists.class)
    public ResponseEntity<HttpResponse> usernameOrEmailExists() {
        return createHttpResponse(BAD_REQUEST, USER_WITH_USERNAME_OR_EMAIL_EXISTS);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<HttpResponse> categoryNotFound() {
        return createHttpResponse(BAD_REQUEST, CATEGORY_NOT_FOUND);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(CategoryWithIdentifierExists.class)
    public ResponseEntity<HttpResponse> categoryByIdentifierExists() {
        return createHttpResponse(BAD_REQUEST, ExceptionMessages.CATEGORY_BY_IDENTIFIER_EXISTS);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CategoryWithNameExists.class)
    public ResponseEntity<HttpResponse> categoryByNameExists() {
        return createHttpResponse(BAD_REQUEST, CATEGORY_BY_NAME_EXISTS);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CategoryWithMediaExistsException.class)
    public ResponseEntity<HttpResponse> categoryWithProvidedMediaExists() {
        return createHttpResponse(BAD_REQUEST, CATEGORY_WITH_PROVIDED_MEDIA_EXISTS);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MediaByNameAlreadyExistsException.class)
    public ResponseEntity<HttpResponse> mediaByNameAlreadyExists() {
        return createHttpResponse(BAD_REQUEST, MEDIA_BY_NAME_ALREADY_EXISTS_EXCEPTION);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MediaBoundToCategoryExistsException.class)
    public ResponseEntity<HttpResponse> MediaBoundToCategoryExists() {
        return createHttpResponse(BAD_REQUEST, MEDIA_BOUND_TO_CATEGORY_EXISTS_EXCEPTION);
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        HttpResponse httpResponse = new HttpResponse(httpStatus.value(),
                httpStatus, httpStatus.getReasonPhrase().toUpperCase(Locale.ROOT), message);
        log.error(message);
        return new ResponseEntity<>(httpResponse, httpStatus);
    }
}
//TODO: Adapt the exceptions into this class so that I can send HttpResponse instance to the front end
//TODO: and print the error in a notification container