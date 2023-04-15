package bg.rborisov.softunigraduation.util.logger;

import bg.rborisov.softunigraduation.enumeration.LoggerStatus;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractLogger {
    private static final String LOG_FOLDER = "C:\\Users\\Radoslav\\Documents\\Github\\softuni-graduation\\Java\\softuni-graduation\\src\\main\\resources\\logs\\";

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected void log(final String fileName, final String message, LoggerStatus loggerStatus) throws IOException {
        File logsFolder = new File(LOG_FOLDER);

        if (!logsFolder.exists()) {
            // Create the logs folder if it doesn't exist
            logsFolder.mkdir();
        }

        File file = new File(LOG_FOLDER + fileName);

        if (!file.exists()) {
            // Create the file if it doesn't exist
            file.createNewFile();
        }

        PrintWriter printWriter = new PrintWriter(new java.io.FileWriter(file, true));

        String loggerBuilder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) +
                " | " + loggerStatus.name() + " | " + message + System.lineSeparator();

        printWriter.print(loggerBuilder);

        printWriter.close();
    }
}