package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.MediaRepository;
import bg.rborisov.softunigraduation.model.Media;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
public class MediaService {
    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public MultipartFile saveFile(MultipartFile media) {
        try {
            String mediaName = Objects.requireNonNull(media.getOriginalFilename()).replaceAll("\\s+", "-");

            StringBuilder mediaNameKey = new StringBuilder();

            for (int i = 0; i < (mediaName.length() <= 5 ? mediaNameKey.length() : 5); i++) {
                int num = mediaName.charAt(i);
                mediaNameKey.append(num);
            }

            mediaNameKey.append(media.getOriginalFilename(), mediaName.length() - 4, mediaName.length());

            this.mediaRepository.save(new Media(mediaNameKey.toString().trim(), media.getBytes()));
        } catch (IOException e) {
            log.error("Temporary store of the media fails ( IOException )");
        }
        return media;
    }

    public byte[] findMediaByName(String name) {
        return this.mediaRepository.findMediaByName(name).orElseThrow().getFile();
    }
}