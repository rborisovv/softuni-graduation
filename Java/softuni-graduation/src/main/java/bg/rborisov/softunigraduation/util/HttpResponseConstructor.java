package bg.rborisov.softunigraduation.util;

import bg.rborisov.softunigraduation.domain.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.Locale;

public final class HttpResponseConstructor {

    public static HttpResponse construct(HttpStatus httpStatus, String message, String notificationStatus) {
        HttpResponse response = new HttpResponse();
        response.setHttpStatus(httpStatus);
        response.setHttpStatusCode(httpStatus.value());
        response.setNotificationStatus(notificationStatus.toLowerCase(Locale.ROOT));
        response.setReason(StringUtils.EMPTY);
        response.setMessage(message);

        return response;
    }
}