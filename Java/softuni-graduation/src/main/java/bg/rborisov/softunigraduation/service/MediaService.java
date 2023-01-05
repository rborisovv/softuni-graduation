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

    public void saveFile(MultipartFile media) {
        Media mediaEntity = null;
        try {
            String mediaName = Objects.requireNonNull(media.getOriginalFilename()).replaceAll("\\s+", "-");

            String PK = RandomStringUtils.randomNumeric(15);

            String mediaUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path(MEDIA_LOCATION_PATH.concat(PK)).toUriString();

            mediaEntity = new Media(mediaName, media.getBytes(), mediaUrl, PK);
            this.mediaRepository.save(mediaEntity);
        } catch (IOException e) {
            log.error("Temporary store of the media fails ( IOException )");
        }
    }

    public byte[] findMediaByName(String name) throws MediaNotFoundException {
        return this.mediaRepository.findMediaByName(name)
                .orElseThrow(() -> new MediaNotFoundException(MEDIA_NOT_FOUND)).getFile();
    }

    public byte[] findMediaByIdentifier(String pkOfFile) throws MediaNotFoundException {
        return this.mediaRepository.findMediaByPkOfFile(pkOfFile)
                .orElseThrow(() -> new MediaNotFoundException(MEDIA_NOT_FOUND)).getFile();
    }
}

//TODO: Create a validation for the media file: Must be an image only

//TODO: Implement a functionality to upload images only seperated from other logic