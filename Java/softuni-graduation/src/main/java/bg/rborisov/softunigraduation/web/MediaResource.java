package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.exception.MediaNotFoundException;
import bg.rborisov.softunigraduation.service.MediaService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medias/sys_master/h4f")
public class MediaResource {
    private final MediaService mediaService;

    public MediaResource(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping(value = "/{pkOfFile}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getMedia(@PathVariable String pkOfFile) throws MediaNotFoundException {
        return this.mediaService.findMediaByIdentifier(pkOfFile);
    }
}