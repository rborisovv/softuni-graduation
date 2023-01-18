package bg.rborisov.softunigraduation.exception;

import bg.rborisov.softunigraduation.domain.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

@Slf4j
public abstract class AbstractExceptionHandler {

    protected ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        HttpResponse httpResponse = new HttpResponse(httpStatus.value(),
                httpStatus, httpStatus.getReasonPhrase().toUpperCase(Locale.ROOT), message);
        log.error(message);
        return new ResponseEntity<>(httpResponse, httpStatus);
    }
}
