package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    public ResponseEntity<HttpResponse> createProduct(ProductDto productDto) {
        System.out.println();
        return null;
    }
}