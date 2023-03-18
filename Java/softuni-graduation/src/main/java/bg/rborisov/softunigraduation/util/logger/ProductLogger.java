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
    private static final String PRODUCT_LOG_FILE_PATH = "Product.txt";

    public final void log(final String message, LoggerStatus loggerStatus) throws IOException {
        super.log(PRODUCT_LOG_FILE_PATH, message, loggerStatus);
    }
}