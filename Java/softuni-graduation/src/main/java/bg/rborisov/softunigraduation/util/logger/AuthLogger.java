package bg.rborisov.softunigraduation.util.logger;

import lombok.*;
import java.io.IOException;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthLogger extends AbstractLogger {
    private static final String AUTH_FILE_PATH = "C:\\Users\\Radoslav\\Documents\\Github\\softuni-graduation\\Java\\softuni-graduation\\src\\main\\resources\\loggers\\Auth.txt";

    public void log(final String message) throws IOException {
        super.log(AUTH_FILE_PATH, message);
    }
}