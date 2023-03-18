package bg.rborisov.softunigraduation.util.logger;

import bg.rborisov.softunigraduation.enumeration.LoggerStatus;
import lombok.*;

import java.io.IOException;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AuthLogger extends AbstractLogger {
    private static final String AUTH_FILE_PATH = "C:\\Users\\Radoslav\\Documents\\Github\\softuni-graduation\\Java\\softuni-graduation\\src\\main\\resources\\logs\\Auth.txt";

    public void log(final String message, LoggerStatus loggerStatus) throws IOException {
        super.log(AUTH_FILE_PATH, message, loggerStatus);
    }
}