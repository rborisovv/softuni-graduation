package bg.rborisov.softunigraduation.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    private bg.rborisov.softunigraduation.model.Media Media;

    @Nullable
    private String categoryIdentifier;
}