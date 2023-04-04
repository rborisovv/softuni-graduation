package bg.rborisov.softunigraduation.domain;

import bg.rborisov.softunigraduation.dto.ProductDto;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BasketHttpResponse extends HttpResponse implements Serializable {
    private Set<ProductDto> basketProducts;
}