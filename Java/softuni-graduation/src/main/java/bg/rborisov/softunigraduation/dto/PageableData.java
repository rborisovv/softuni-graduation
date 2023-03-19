package bg.rborisov.softunigraduation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableData {

    @NotNull
    @PositiveOrZero
    private Integer pageIndex;

    @NotNull
    @PositiveOrZero
    private Integer pageSize;
}