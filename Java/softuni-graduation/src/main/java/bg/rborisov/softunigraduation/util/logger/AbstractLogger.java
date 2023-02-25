package bg.rborisov.softunigraduation.util.logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class AbstractLogger {
    public void log(final String filePath, final String message) throws IOException {
        File file = new File(filePath);
        PrintWriter printWriter = new PrintWriter(new java.io.FileWriter(file, true));

        printWriter.write(message);
        printWriter.print(System.lineSeparator());

        printWriter.close();
    }
}