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
public class OrderLogger extends AbstractLogger {
    private static final String ORDER_LOG_FILE_PATH = "Order.txt";

    public final void log(final String message, LoggerStatus loggerStatus) throws IOException {
        super.log(ORDER_LOG_FILE_PATH, message, loggerStatus);
    }
}