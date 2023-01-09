package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.dao.MediaRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.MediaDto;
import bg.rborisov.softunigraduation.exception.MediaAlreadyExistsException;
import bg.rborisov.softunigraduation.exception.MediaNotFoundException;
import bg.rborisov.softunigraduation.service.MediaService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static bg.rborisov.softunigraduation.constant.FileConstant.PERCENT;

@RestController
@RequestMapping("/media")
public class MediaResource {
    private final MediaService mediaService;

    private final MediaRepository mediaRepository;

    private final ModelMapper modelMapper;

    public MediaResource(MediaService mediaService, MediaRepository mediaRepository, ModelMapper modelMapper) {
        this.mediaService = mediaService;
        this.mediaRepository = mediaRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/sys_master/h4f/{pkOfFile}.{ext}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getMedia(@PathVariable String pkOfFile) throws MediaNotFoundException {
        return this.mediaService.findMediaByPk(pkOfFile);
    }

    @PostMapping("/filter")
    public Set<MediaDto> filterMediaByName(@RequestBody String name) {
        return this.mediaRepository.findMediaByNameLike(PERCENT + name + PERCENT)
                .stream().map(media -> modelMapper.map(media, MediaDto.class))
                .collect(Collectors.toSet());
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createMedia(@RequestParam("name") String name,
                                                    @RequestParam("file") MultipartFile file) throws MediaAlreadyExistsException, IOException {

        @Valid MediaDto mediaDto = MediaDto.builder()
                .name(name)
                .multipartFile(file)
                .build();
        return this.mediaService.createMedia(mediaDto);

        //TODO: Catch exception
    }

    @PostMapping("/findByName")
    public boolean isMediaByNamePresent(@RequestBody String name) {
        return this.mediaRepository.findMediaByName(name).isPresent();
    }
}