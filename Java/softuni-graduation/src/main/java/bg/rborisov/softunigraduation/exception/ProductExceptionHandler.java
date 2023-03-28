package bg.rborisov.softunigraduation.exception;

import bg.rborisov.softunigraduation.domain.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ProductExceptionHandler extends AbstractExceptionHandler {

    @ExceptionHandler(AbsentCategoryProductException.class)
    public ResponseEntity<HttpResponse> categoryAbsentFromProduct() {
        return super.createHttpResponse(BAD_REQUEST, PRODUCT_WITHOUT_CATEGORY);
    }

    @ExceptionHandler(AbsentMediaOnProductException.class)
    public ResponseEntity<HttpResponse> mediaAbsentFromProduct() {
        return super.createHttpResponse(BAD_REQUEST, PRODUCT_WITHOUT_MEDIA);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<HttpResponse> productNotFound() {
        return super.createHttpResponse(BAD_REQUEST, PRODUCT_COULD_NOT_BE_FOUND);
    }

    @ExceptionHandler(ProductSoldOutException.class)
    public ResponseEntity<HttpResponse> productSoldOut() {
        return super.createHttpResponse(BAD_REQUEST, PRODUCT_SOLD_OUT);
    }
}