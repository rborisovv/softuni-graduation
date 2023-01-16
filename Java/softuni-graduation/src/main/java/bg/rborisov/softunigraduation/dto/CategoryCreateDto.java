package bg.rborisov.softunigraduation.dto;

import bg.rborisov.softunigraduation.util.validators.ImageValidator;
import jakarta.annotation.Nullable;
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
public class CategoryCreateDto implements Serializable {
    @Size(min = 4, max = 40)
    @NonNull
    private String name;

    @Size(min = 4, max = 10)
    @NonNull
    private String identifier;

    @Size(max = 30)
    @Nullable
    private String productNamePrefix;

    @NonNull
    @NotNull
    @ImageValidator
    private MultipartFile media;

    private String mediaUrl;

    @org.springframework.lang.Nullable
    private MediaDto selectedMediaDto;
}
