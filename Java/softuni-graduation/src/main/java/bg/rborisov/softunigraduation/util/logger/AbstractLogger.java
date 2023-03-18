package bg.rborisov.softunigraduation.util.logger;

import bg.rborisov.softunigraduation.enumeration.LoggerStatus;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractLogger {
    public static final String LOG_FOLDER = "C:\\Users\\Radoslav\\Documents\\Github\\softuni-graduation\\Java\\softuni-graduation\\src\\main\\resources\\loggers\\";

    public void log(final String fileName, final String message, LoggerStatus loggerStatus) throws IOException {
        File file = new File(LOG_FOLDER + fileName);
        PrintWriter printWriter = new PrintWriter(new java.io.FileWriter(file, true));

        String loggerBuilder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) +
                " | " + loggerStatus.name() + " | " + message + System.lineSeparator();

        printWriter.print(loggerBuilder);

        printWriter.close();
    }
}