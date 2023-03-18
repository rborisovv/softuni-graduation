package bg.rborisov.softunigraduation.util.logger;

import bg.rborisov.softunigraduation.enumeration.LoggerStatus;
import lombok.*;

import java.io.IOException;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AuthLogger extends AbstractLogger {
    private static final String AUTH_LOG_FILE = "Auth.txt";

    public void log(final String message, LoggerStatus loggerStatus) throws IOException {
        super.log(AUTH_LOG_FILE, message, loggerStatus);
    }
}