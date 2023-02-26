package bg.rborisov.softunigraduation.util.logger;

import bg.rborisov.softunigraduation.enumeration.LoggerStatus;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractLogger {
    public void log(final String filePath, final String message, LoggerStatus loggerStatus) throws IOException {
        File file = new File(filePath);
        PrintWriter printWriter = new PrintWriter(new java.io.FileWriter(file, true));

        String loggerBuilder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) +
                " | " + LoggerStatus.INFO.name() + " | " + message + System.lineSeparator();

        printWriter.print(loggerBuilder);

        printWriter.close();
    }
}