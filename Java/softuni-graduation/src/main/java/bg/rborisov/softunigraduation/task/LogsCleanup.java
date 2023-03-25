package bg.rborisov.softunigraduation.task;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static bg.rborisov.softunigraduation.constant.FileConstant.FILE_TXT_EXTENSION;

@Component
public class LogsCleanup {
    private static final String LOG_FOLDER = "C:\\Users\\Radoslav\\Documents\\Github\\softuni-graduation\\Java\\softuni-graduation\\src\\main\\resources\\logs\\";

    @Scheduled(cron = "@daily")
    public void deleteLogsFromFile() throws IOException {
        Path folder = Paths.get(LOG_FOLDER);

        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(FILE_TXT_EXTENSION))
                    .forEach(path -> {
                        try {
                            Files.write(path, StringUtils.EMPTY.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}