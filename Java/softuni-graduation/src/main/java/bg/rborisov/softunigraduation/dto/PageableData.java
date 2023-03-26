package bg.rborisov.softunigraduation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableData implements Serializable {

    @NotNull
    @PositiveOrZero
    private Integer pageIndex;

    @NotNull
    @PositiveOrZero
    private Integer pageSize;
}