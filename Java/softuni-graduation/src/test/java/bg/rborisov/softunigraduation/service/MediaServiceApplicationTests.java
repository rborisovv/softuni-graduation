package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.MediaRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.exception.MediaByNameAlreadyExistsException;
import bg.rborisov.softunigraduation.exception.MediaNotFoundException;
import bg.rborisov.softunigraduation.model.Media;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static bg.rborisov.softunigraduation.common.Messages.MEDIA_DELETED_SUCCESSFULLY;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MediaServiceApplicationTests {
    @MockBean
    private MediaRepository mediaRepository;

    @Autowired
    private MediaService mediaService;

    private Media media;

    @BeforeEach
    void init() {
        this.media = Media.builder().name("media").mediaUrl("media://url").file(null).creationTime(LocalDate.now()).build();
        this.media.setFile(new byte[1]);
    }

    @Test
    void shouldThrowIfMediaWithSameNameIsPresent() {
        Mockito.when(this.mediaRepository.findMediaByName("media")).thenReturn(Optional.of(media));
        assertThrows(MediaByNameAlreadyExistsException.class,
                () -> this.mediaService.saveMedia("media", null));
    }

    @Test
    void shouldSuccessfullySaveMedia() throws MediaByNameAlreadyExistsException, IOException {
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(this.mediaRepository.findMediaByName("media")).thenReturn(Optional.empty());
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("media.jpeg");

        assertEquals(0, this.mediaRepository.findAll().size());
        this.mediaService.saveMedia("media", multipartFile);
    }

    @Test
    void shouldFailWhenNoMediaFound() {
        Mockito.when(this.mediaRepository.findMediaByPkOfFile("media")).thenReturn(Optional.empty());
        assertThrows(MediaNotFoundException.class, () -> this.mediaService.findMediaByPk("media"));
    }

    @Test
    void shouldSuccessfullyReturnMediaContent() throws MediaNotFoundException {
        Mockito.when(this.mediaRepository.findMediaByPkOfFile("media")).thenReturn(Optional.of(this.media));
        byte[] mediaContent = this.mediaService.findMediaByPk("media");
        assertNotNull(mediaContent);
    }

    @Test
    void shouldSuccessfullyRemoveMedia() throws MediaNotFoundException {
        Mockito.when(this.mediaRepository.findMediaByPkOfFile("media")).thenReturn(Optional.of(this.media));
        ResponseEntity<HttpResponse> response = this.mediaService.deleteMedia("media");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(String.format(MEDIA_DELETED_SUCCESSFULLY, this.media.getName()),
                Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void shouldThrowIfMediaByNameExists() {
        Mockito.when(this.mediaRepository.findMediaByName("media")).thenReturn(Optional.of(new Media()));
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        assertThrows(MediaByNameAlreadyExistsException.class,
                () -> this.mediaService.constructMediaForEntity(multipartFile, "media.png"));
    }

    @Test
    void shouldConstructMedia() throws MediaByNameAlreadyExistsException, IOException {
        Mockito.when(this.mediaRepository.findMediaByName("media")).thenReturn(Optional.empty());
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("media");

        Optional<Media> response = this.mediaService.constructMediaForEntity(multipartFile, "media");

        response.ifPresent(Assertions::assertNotNull);
    }
}