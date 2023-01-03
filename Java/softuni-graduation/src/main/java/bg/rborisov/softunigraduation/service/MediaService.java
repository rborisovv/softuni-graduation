package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.MediaRepository;
import bg.rborisov.softunigraduation.exception.MediaNotFoundException;
import bg.rborisov.softunigraduation.model.Media;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Objects;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.MEDIA_NOT_FOUND;
import static bg.rborisov.softunigraduation.constant.FileConstant.MEDIA_LOCATION_PATH;

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
            mediaNameKey.append(RandomStringUtils.randomNumeric(15));
            mediaNameKey.append(media.getOriginalFilename(), mediaName.length() - 4, mediaName.length());

            String mediaUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path(MEDIA_LOCATION_PATH.concat(mediaNameKey.toString().trim())).toUriString();

            this.mediaRepository.save(new Media(mediaNameKey.toString().trim(), media.getBytes(), mediaUrl, mediaName));
        } catch (IOException e) {
            log.error("Temporary store of the media fails ( IOException )");
        }
        return media;
    }

    public byte[] findMediaByName(String name) {
        try {
            return this.mediaRepository.findMediaByName(name)
                    .orElseThrow(() -> new MediaNotFoundException(MEDIA_NOT_FOUND)).getFile();
        } catch (MediaNotFoundException e) {
            log.error(MEDIA_NOT_FOUND);
            throw new RuntimeException(e);
        }
    }
}

//TODO: Create a validation for the media file: Must be an image only

//TODO: Implement a functionality to upload images only seperated from other logic