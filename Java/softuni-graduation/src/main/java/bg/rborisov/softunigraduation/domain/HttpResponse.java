package bg.rborisov.softunigraduation.domain;

import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class HttpResponse implements Serializable {

    @NonNull
    private int httpStatusCode;

    @NonNull
    private HttpStatus httpStatus;

    @NonNull
    private String reason;

    @NonNull
    private String message;

    private String notificationStatus;
}