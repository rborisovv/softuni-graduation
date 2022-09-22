package bg.rborisov.softunigraduation.domain;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class HttpResponse {

    private int httpStatusCode;

    private HttpStatus httpStatus;

    private String reason;

    private String message;
}