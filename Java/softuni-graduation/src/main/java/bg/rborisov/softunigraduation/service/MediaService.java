package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.MediaRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.exception.MediaByNameAlreadyExistsException;
import bg.rborisov.softunigraduation.exception.MediaNotFoundException;
import bg.rborisov.softunigraduation.model.Media;
import bg.rborisov.softunigraduation.util.AbstractMediaUrlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.MEDIA_NOT_FOUND;
import static bg.rborisov.softunigraduation.common.Messages.MEDIA_DELETED_SUCCESSFULLY;

@Service
@Slf4j
public class MediaService extends AbstractMediaUrlBuilder {
    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 20)
    public byte[] findMediaByPk(String pkOfFile) throws MediaNotFoundException {
        return this.mediaRepository.findMediaByPkOfFile(pkOfFile)
                .orElseThrow(() -> new MediaNotFoundException(MEDIA_NOT_FOUND)).getFile();
    }

    public void saveMedia(String name, MultipartFile multipartFile) throws MediaByNameAlreadyExistsException, IOException {
        if (this.mediaRepository.findMediaByName(name).isPresent()) {
            throw new MediaByNameAlreadyExistsException();
        }

        String PK = RandomStringUtils.randomNumeric(15);
        String mediaUrl = super.constructMediaUrl(PK, multipartFile);
        LocalDate creationTime = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        creationTime.format(dateTimeFormatter);
        Media media = new Media(name, multipartFile.getBytes(), mediaUrl, PK, creationTime);
        this.mediaRepository.save(media);
    }

    public ResponseEntity<HttpResponse> deleteMedia(String pk) throws MediaNotFoundException {
        Media media = this.mediaRepository.findMediaByPkOfFile(pk).orElseThrow(MediaNotFoundException::new);
        this.mediaRepository.deleteById(media.getId());

        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, StringUtils.EMPTY,
                String.format(MEDIA_DELETED_SUCCESSFULLY, media.getName()));
        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }

    public Optional<Media> constructMediaForEntity(final MultipartFile multipartFile, final String mediaName) throws MediaByNameAlreadyExistsException, IOException {
        Optional<Media> optionalMedia;
        optionalMedia = this.mediaRepository.findMediaByName(mediaName.substring(0, mediaName.length() - 4));

        if (optionalMedia.isPresent()) {
            throw new MediaByNameAlreadyExistsException();
        }

        this.saveMedia(multipartFile.getOriginalFilename(), multipartFile);
        optionalMedia = this.mediaRepository.findMediaByName(multipartFile.getOriginalFilename());
        return optionalMedia;
    }
}