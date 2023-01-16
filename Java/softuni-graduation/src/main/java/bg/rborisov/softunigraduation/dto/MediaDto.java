package bg.rborisov.softunigraduation.dto;

import bg.rborisov.softunigraduation.util.validators.ImageValidator;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class MediaDto implements Serializable {
    @NotBlank
    @Size(min = 3, max = 40)
    private String name;

    @Nullable
    private String pkOfFile;

    private String mediaUrl;

    @NonNull
    @NotNull
    @ImageValidator
    private MultipartFile multipartFile;

    @NotBlank
    private String selectedTypeSubject;
}