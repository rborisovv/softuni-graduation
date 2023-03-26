package bg.rborisov.softunigraduation.dto;

import bg.rborisov.softunigraduation.util.validators.ImageValidator;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class CategoryDto implements Serializable {

    @Size(min = 4, max = 40)
    @NonNull
    private String name;

    @Size(min = 4, max = 10)
    @NonNull
    private String identifier;

    @Size(min = 4, max = 30)
    @NotBlank
    private String superCategory;

    @NotNull
    @ImageValidator
    private MultipartFile media;

    private String mediaUrl;

    @Nullable
    private List<ProductDto> products;

    @Nullable
    private Map<String, String> breadcrumb;
}