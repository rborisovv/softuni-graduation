package bg.rborisov.softunigraduation.exception;

import bg.rborisov.softunigraduation.domain.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.ABSENT_VOUCHER_BY_NAME;
import static bg.rborisov.softunigraduation.common.ExceptionMessages.USER_HAS_NO_BASKET;
import static bg.rborisov.softunigraduation.common.ExceptionMessages.VOUCHER_BY_NAME_PRESENT;
import static bg.rborisov.softunigraduation.common.ExceptionMessages.VOUCHER_CANNOT_BE_USED_BY_USER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class VoucherExceptionHandler extends AbstractExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(VoucherByNameAlreadyPresent.class)
    public ResponseEntity<HttpResponse> voucherByNamePresent() {
        return super.createHttpResponse(BAD_REQUEST, VOUCHER_BY_NAME_PRESENT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AbsentVoucherByNameException.class)
    public ResponseEntity<HttpResponse> voucherByNameNotPresent() {
        return super.createHttpResponse(BAD_REQUEST, ABSENT_VOUCHER_BY_NAME);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(VoucherExpiredException.class)
    public ResponseEntity<HttpResponse> voucherExpired(VoucherExpiredException ex) {
        return super.createHttpResponse(BAD_REQUEST, ex.getMessage());
        //TODO: Check if passing this param works
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(VoucherCannotBeUsedByUserException.class)
    public ResponseEntity<HttpResponse> voucherCannotBeUsedByUser() {
        return super.createHttpResponse(BAD_REQUEST, VOUCHER_CANNOT_BE_USED_BY_USER);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserHasNoBasketException.class)
    public ResponseEntity<HttpResponse> noBasketFoundForUser() {
        return super.createHttpResponse(BAD_REQUEST, USER_HAS_NO_BASKET);
    }
}