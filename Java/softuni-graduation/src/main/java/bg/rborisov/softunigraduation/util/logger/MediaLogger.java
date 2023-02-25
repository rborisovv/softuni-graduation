package bg.rborisov.softunigraduation.util.logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class MediaLogger extends AbstractLogger {
    private static final String MEDIA_FILE_PATH = "C:\\Users\\Radoslav\\Documents\\Github\\softuni-graduation\\Java\\softuni-graduation\\src\\main\\resources\\loggers\\Media.txt";

    public final void log(final String message) throws IOException {
        super.log(MEDIA_FILE_PATH, message);
    }
}