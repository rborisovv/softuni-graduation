package bg.rborisov.softunigraduation.exception;

import bg.rborisov.softunigraduation.common.ExceptionMessages;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.CATEGORY_BY_NAME_EXISTS;
import static bg.rborisov.softunigraduation.common.ExceptionMessages.CATEGORY_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class CategoryExceptionHandler extends AbstractExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<HttpResponse> categoryNotFound() {
        return super.createHttpResponse(BAD_REQUEST, CATEGORY_NOT_FOUND);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(CategoryWithIdentifierExists.class)
    public ResponseEntity<HttpResponse> categoryByIdentifierExists() {
        return super.createHttpResponse(BAD_REQUEST, ExceptionMessages.CATEGORY_BY_IDENTIFIER_EXISTS);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AbsentCategoryByIdentifier.class)
    public ResponseEntity<HttpResponse> absentCategoryByIdentifier() {
        return super.createHttpResponse(BAD_REQUEST, ExceptionMessages.ABSENT_CATEGORY_BY_IDENTIFIER);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CategoryWithNameExists.class)
    public ResponseEntity<HttpResponse> categoryByNameExists() {
        return super.createHttpResponse(BAD_REQUEST, CATEGORY_BY_NAME_EXISTS);
    }
}