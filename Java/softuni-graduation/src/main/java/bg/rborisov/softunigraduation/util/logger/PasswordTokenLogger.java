package bg.rborisov.softunigraduation.util.logger;

import bg.rborisov.softunigraduation.enumeration.LoggerStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PasswordTokenLogger extends AbstractLogger {
    private static final String PASSWORD_TOKEN_CLEANUP_LOG_PATH = "C:\\Users\\Radoslav\\Documents\\Github\\softuni-graduation\\Java\\softuni-graduation\\src\\main\\resources\\loggers\\PasswordTokenCleanup.txt";

    public void log(final String message, LoggerStatus loggerStatus) throws IOException {
        super.log(PASSWORD_TOKEN_CLEANUP_LOG_PATH, message, loggerStatus);
    }
}