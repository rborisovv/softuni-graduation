package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.dao.ProductRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.ProductDto;
import bg.rborisov.softunigraduation.service.ProductService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductResource {
    @Resource
    private ProductRepository productRepository;

    @Resource
    private ProductService productService;

    @PostMapping("/findByName")
    public boolean findProductByName(@RequestBody String name) {
        return this.productRepository.findProductByName(name).isPresent();
    }

    @PostMapping("/findByIdentifier")
    public boolean findProductByIdentifier(@RequestBody String identifier) {
        return this.productRepository.findProductByIdentifier(identifier).isPresent();
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createProduct(@Valid @RequestBody ProductDto productDto) {
        return this.productService.createProduct(productDto);
    }
}