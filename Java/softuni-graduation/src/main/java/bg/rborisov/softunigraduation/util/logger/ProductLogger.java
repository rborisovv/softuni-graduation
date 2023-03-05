package bg.rborisov.softunigraduation.util.logger;

import bg.rborisov.softunigraduation.enumeration.LoggerStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductLogger extends AbstractLogger {
    private static final String PRODUCT_FILE_PATH = "C:\\Users\\Radoslav\\Documents\\Github\\softuni-graduation\\Java\\softuni-graduation\\src\\main\\resources\\loggers\\Product.txt";

    public final void log(final String message, LoggerStatus loggerStatus) throws IOException {
        super.log(PRODUCT_FILE_PATH, message, loggerStatus);
    }
}