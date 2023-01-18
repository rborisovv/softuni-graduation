package bg.rborisov.softunigraduation.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ProductDto implements Serializable {

    @NotBlank
    @Size(min = 4, max = 120)
    private String name;

    @NotBlank
    @Size(min = 4, max = 10)
    private String identifier;

    @Size(max = 255)
    private String description;

    @PositiveOrZero
    private BigDecimal price;

    @Nullable
    private MultipartFile Media;
    @Nullable
    private String pkOfFile;

    private String mediaUrl;

    @Nullable
    private String categoryIdentifier;

    @Nullable
    private String categoryMediaUrl;
}