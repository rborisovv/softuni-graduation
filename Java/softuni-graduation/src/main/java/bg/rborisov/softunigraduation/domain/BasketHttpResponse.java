package bg.rborisov.softunigraduation.domain;

import bg.rborisov.softunigraduation.dto.ProductDto;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BasketHttpResponse extends HttpResponse {
    private Set<ProductDto> basketProducts;
}