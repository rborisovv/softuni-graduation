package bg.rborisov.softunigraduation.dto;

import bg.rborisov.softunigraduation.model.Category;
import bg.rborisov.softunigraduation.model.Product;
import bg.rborisov.softunigraduation.util.validators.ImageValidator;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CategoryDto implements Serializable {

    @Size(min = 4, max = 40)
    @NonNull
    private String name;

    @Size(min = 4, max = 10)
    @NonNull
    private String identifier;

    @Size(min = 4, max = 30)
    @NotBlank
    private String superCategoryIdentifier;

    @NotNull
    @ImageValidator
    private MultipartFile media;

    private String mediaUrl;

    @Nullable
    private Set<Product> products;

    @Nullable
    private Map<String, String> breadcrumb;
}