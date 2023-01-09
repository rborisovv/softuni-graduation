package bg.rborisov.softunigraduation.dto;

import bg.rborisov.softunigraduation.util.validators.ImageValidator;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CategoryUpdateDto {
    @NonNull
    @NotBlank
    @Size(min = 4, max = 40)
    private String name;

    @NonNull
    @NotBlank
    @Size(min = 5, max = 40)
    private String oldName;

    @NonNull
    @NotBlank
    @Size(min = 4, max = 10)
    private String identifier;

    @NonNull
    @NotBlank
    @Size(min = 4, max = 10)
    private String oldIdentifier;

    @Nullable
    @NotBlank
    @Size(max = 30)

    private String productNamePrefix;

    @Nullable
    @ImageValidator
    private MultipartFile media;

    private String mediaUrl;
}