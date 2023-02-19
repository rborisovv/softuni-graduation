package bg.rborisov.softunigraduation.domain;

import bg.rborisov.softunigraduation.dto.ProductDto;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FavouritesHttpResponse extends HttpResponse {
    private Set<ProductDto> favouriteProducts;
}