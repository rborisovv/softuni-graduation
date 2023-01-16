package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.MediaRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.MediaDto;
import bg.rborisov.softunigraduation.enumeration.MediaTypeEnum;
import bg.rborisov.softunigraduation.exception.MediaBoundToCategoryExistsException;
import bg.rborisov.softunigraduation.exception.MediaByNameAlreadyExistsException;
import bg.rborisov.softunigraduation.exception.MediaNotFoundException;
import bg.rborisov.softunigraduation.model.Media;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.MEDIA_NOT_FOUND;
import static bg.rborisov.softunigraduation.common.Messages.MEDIA_CREATED_SUCCESSFULLY;
import static bg.rborisov.softunigraduation.common.Messages.MEDIA_DELETED_SUCCESSFULLY;
import static bg.rborisov.softunigraduation.constant.FileConstant.MEDIA_LOCATION_PATH;

@Service
@Slf4j
public class MediaService {
    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public void saveMediaForCategory(MultipartFile media, MediaTypeEnum mediaTypeSubject) throws MediaBoundToCategoryExistsException {
        try {
            String originalFileName = Objects.requireNonNull(media.getOriginalFilename()).replaceAll("\\s+", "-");
            String mediaName = originalFileName.substring(0, originalFileName.length() - 4);

            Optional<Media> optionalMedia = this.mediaRepository.findMediaByName(mediaName);

            if (optionalMedia.isPresent() && optionalMedia.get().getCategory() != null) {
                throw new MediaBoundToCategoryExistsException();
            }

            String PK = RandomStringUtils.randomNumeric(15);
            String mediaUrl = constructMediaUrl(PK, media);

            Media mediaEntity = new Media(mediaName, media.getBytes(), mediaUrl, PK, null, mediaTypeSubject);
            this.mediaRepository.save(mediaEntity);

        } catch (IOException e) {
            log.error("Temporary store of the media fails ( IOException )");
        }
    }

    public byte[] findMediaByName(String name) throws MediaNotFoundException {
        return this.mediaRepository.findMediaByName(name)
                .orElseThrow(() -> new MediaNotFoundException(MEDIA_NOT_FOUND)).getFile();
    }

    public byte[] findMediaByPk(String pkOfFile) throws MediaNotFoundException {
        return this.mediaRepository.findMediaByPkOfFile(pkOfFile)
                .orElseThrow(() -> new MediaNotFoundException(MEDIA_NOT_FOUND)).getFile();
    }

    public ResponseEntity<HttpResponse> createMedia(MediaDto mediaDto) throws MediaByNameAlreadyExistsException, IOException {
        if (this.mediaRepository.findMediaByName(mediaDto.getName()).isPresent()) {
            throw new MediaByNameAlreadyExistsException();
        }

        String PK = RandomStringUtils.randomNumeric(15);
        String mediaUrl = constructMediaUrl(PK, mediaDto.getMultipartFile());
        Media media = new Media();

        if (mediaDto.getSelectedTypeSubject().equalsIgnoreCase(MediaTypeEnum.CATEGORY.name())) {
            media = new Media(mediaDto.getName(), mediaDto.getMultipartFile().getBytes(), mediaUrl, PK, null, MediaTypeEnum.CATEGORY);
        } else if (mediaDto.getSelectedTypeSubject().equalsIgnoreCase(MediaTypeEnum.PRODUCT.name())) {
            media = new Media(mediaDto.getName(), mediaDto.getMultipartFile().getBytes(), mediaUrl, PK, null, MediaTypeEnum.PRODUCT);
        }

        this.mediaRepository.save(media);

        HttpResponse httpResponse = HttpResponse.builder().httpStatusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK).reason("").message(MEDIA_CREATED_SUCCESSFULLY)
                .build();


        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }

    private String constructMediaUrl(String PK, MultipartFile media) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(MEDIA_LOCATION_PATH.concat(PK).concat(Objects.requireNonNull(media.getOriginalFilename())
                        .substring(media.getOriginalFilename().length() - 4)))
                .toUriString();
    }

    public ResponseEntity<HttpResponse> deleteMedia(String pk) throws MediaNotFoundException {
        Media media = this.mediaRepository.findMediaByPkOfFile(pk).orElseThrow(MediaNotFoundException::new);
        this.mediaRepository.deleteById(media.getId());

        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, "",
                String.format(MEDIA_DELETED_SUCCESSFULLY, media.getName()));
        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }
}

//TODO: Create a validation for the media file: Must be an image only