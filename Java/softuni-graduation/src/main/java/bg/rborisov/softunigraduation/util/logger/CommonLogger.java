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
public class CommonLogger extends AbstractLogger {
    private static final String COMMON_LOG_FILE_PATH = "Common.txt";

    public final void log(final String message, LoggerStatus loggerStatus) throws IOException {
        super.log(COMMON_LOG_FILE_PATH, message, loggerStatus);
    }
}