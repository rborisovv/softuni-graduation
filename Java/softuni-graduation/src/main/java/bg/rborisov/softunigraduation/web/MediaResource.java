package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.dao.MediaRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.MediaDto;
import bg.rborisov.softunigraduation.dto.PageableData;
import bg.rborisov.softunigraduation.exception.MediaByNameAlreadyExistsException;
import bg.rborisov.softunigraduation.exception.MediaNotFoundException;
import bg.rborisov.softunigraduation.service.MediaService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static bg.rborisov.softunigraduation.common.Messages.MEDIA_CREATED_SUCCESSFULLY;
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

    @Cacheable("media")
    @GetMapping(value = "/*/h4f/{pkOfFile}.*", produces = MediaType.IMAGE_JPEG_VALUE)
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
                                                    @RequestParam("file") MultipartFile file) throws MediaByNameAlreadyExistsException, IOException {

        this.mediaService.saveMedia(name, file);

        HttpResponse httpResponse = HttpResponse.builder().httpStatusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK).reason("").message(MEDIA_CREATED_SUCCESSFULLY).build();

        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }

    @PostMapping("/findByName")
    public boolean isMediaByNamePresent(@RequestBody String name) {
        return this.mediaRepository.findMediaByName(name).isPresent();
    }

    @PostMapping("/findAll")
    public Page<MediaDto> findAllMedias(final @Valid @RequestBody PageableData pageableData) {
        Pageable pageable = PageRequest.of(pageableData.getPageIndex(), pageableData.getPageSize());

        return this.mediaRepository.findAll(pageable)
                .map(media -> this.modelMapper.map(media, MediaDto.class));
    }

    @DeleteMapping("/delete/{pk}")
    public ResponseEntity<HttpResponse> deleteMedia(@PathVariable String pk) throws MediaNotFoundException {
        return this.mediaService.deleteMedia(pk);
    }
}