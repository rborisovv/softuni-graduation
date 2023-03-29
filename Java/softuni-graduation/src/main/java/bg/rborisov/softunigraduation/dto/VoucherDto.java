package bg.rborisov.softunigraduation.dto;

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
    private String name;

    private String type;

    private BigDecimal discount;

    private String creationDate;

    private String expirationDate;
}