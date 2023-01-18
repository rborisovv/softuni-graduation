package bg.rborisov.softunigraduation.util;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

import static bg.rborisov.softunigraduation.constant.FileConstant.MEDIA_LOCATION_PATH;

public abstract class AbstractMediaUrlBuilder {

    protected String constructMediaUrl(String PK, MultipartFile media) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(MEDIA_LOCATION_PATH.concat(PK).concat(Objects.requireNonNull(media.getOriginalFilename())
                        .substring(media.getOriginalFilename().length() - 4)))
                .toUriString();
    }
}