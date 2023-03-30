package bg.rborisov.softunigraduation.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDto implements Serializable {

    @NotBlank
    @Size(min = 3, max = 20)
    private String name;

    @NotBlank
    private String type;

    @NotNull
    @Positive
    private BigDecimal discount;

    private LocalDate creationDate;

    @NotNull
    @FutureOrPresent
    private LocalDate expirationDate;

    @Nullable
    private UserDto user;

    @Nullable
    private CategoryDto category;
}