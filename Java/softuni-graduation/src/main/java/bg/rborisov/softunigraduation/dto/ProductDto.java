package bg.rborisov.softunigraduation.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
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
    private MultipartFile media;

    @NotNull
    @PositiveOrZero
    private Integer stockLevel;

    @PositiveOrZero
    private Integer quantity;

    @NotNull
    private Boolean showBuyButton;

    @Nullable
    private String pkOfFile;

    private String mediaUrl;

    private String categoryName;

    @Nullable
    private String categoryIdentifier;

    @Nullable
    private String categoryMediaUrl;

    @Nullable
    private LocalDate bestBefore;
}