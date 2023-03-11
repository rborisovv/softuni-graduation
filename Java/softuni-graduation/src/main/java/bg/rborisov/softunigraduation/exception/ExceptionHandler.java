package bg.rborisov.softunigraduation.exception;

import bg.rborisov.softunigraduation.common.ExceptionMessages;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ExceptionHandler extends AbstractExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> accountLockedException() {
        return super.createHttpResponse(BAD_REQUEST, ACCOUNT_LOCKED);
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
}