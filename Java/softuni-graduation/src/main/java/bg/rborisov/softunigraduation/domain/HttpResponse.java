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
    protected int httpStatusCode;
    @NonNull
    protected HttpStatus httpStatus;
    @NonNull
    protected String reason;
    @NonNull
    protected String message;
    protected String notificationStatus;
}