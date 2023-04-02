package bg.rborisov.softunigraduation.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static bg.rborisov.softunigraduation.constant.FileConstant.MEDIA_LOCATION_PATH;

public abstract class AbstractMediaUrlBuilder {

    protected String constructMediaUrl(String PK, MultipartFile media) {
        String fileName = media.getOriginalFilename();
        String ext = StringUtils.EMPTY;

        assert fileName != null;
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            ext = fileName.substring(i);
        }

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(MEDIA_LOCATION_PATH.concat(PK).concat(ext)).toUriString();
    }
}