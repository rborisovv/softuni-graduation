package bg.rborisov.softunigraduation.dto;

import jakarta.annotation.Nullable;
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
public class CategoryDto implements Serializable {

    @Size(min = 5, max = 40)
    @NonNull
    private String name;

    @Size(min = 4, max = 10)
    @NonNull
    private String categoryIdentifier;

    @Size(min = 30)
    @Nullable
    private String productNamePrefix;

    @NonNull
    private MultipartFile media;
}