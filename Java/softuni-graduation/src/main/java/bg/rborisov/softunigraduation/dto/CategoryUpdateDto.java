package bg.rborisov.softunigraduation.dto;

import bg.rborisov.softunigraduation.util.validators.ImageValidator;
import jakarta.annotation.Nullable;
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
    @Size(min = 5, max = 40)
    @NonNull
    private String name;

    @Size(min = 5, max = 40)
    @NonNull
    private String oldName;

    @Size(min = 4, max = 10)
    @NonNull
    private String identifier;

    @NonNull
    @Size(min = 4, max = 10)
    private String oldIdentifier;

    @Size(max = 30)
    @Nullable
    private String productNamePrefix;

    @Nullable
    @ImageValidator
    private MultipartFile media;

    private String mediaUrl;
}