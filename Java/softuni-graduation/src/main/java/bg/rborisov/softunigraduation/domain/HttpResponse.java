package bg.rborisov.softunigraduation.domain;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class HttpResponse implements Serializable {

    private int httpStatusCode;

    private HttpStatus httpStatus;

    private String reason;

    private String message;
}