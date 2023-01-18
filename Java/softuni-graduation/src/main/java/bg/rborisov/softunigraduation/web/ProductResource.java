package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.dao.ProductRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.ProductDto;
import bg.rborisov.softunigraduation.exception.*;
import bg.rborisov.softunigraduation.service.ProductService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductResource {
    @Resource
    private ProductRepository productRepository;
    @Resource
    private ProductService productService;
    @Resource
    private ModelMapper modelMapper;

    @PostMapping("/findByName")
    public boolean findProductByName(@RequestBody String name) {
        return this.productRepository.findProductByName(name).isPresent();
    }

    @PostMapping("/findByIdentifier")
    public boolean findProductByIdentifier(@RequestBody String identifier) {
        return this.productRepository.findProductByIdentifier(identifier).isPresent();
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createProduct(@RequestParam("name") String name,
                                                      @RequestParam("identifier") String identifier,
                                                      @RequestParam("price") BigDecimal price,
                                                      @RequestParam("description") String description,
                                                      @RequestParam("media") MultipartFile multipartFile,
                                                      @RequestParam("pkOfFile") String pkOfFile,
                                                      @RequestParam("categoryIdentifier") String categoryIdentifier) throws AbsentMediaOnProductException,
            AbsentCategoryProductException, MediaByNameAlreadyExistsException, MediaNotFoundException, CategoryNotFoundException, IOException {

        @Valid ProductDto productDto = ProductDto.builder()
                .name(name).identifier(identifier)
                .price(price).description(description).Media(multipartFile)
                .pkOfFile(pkOfFile).categoryIdentifier(categoryIdentifier).build();

        return this.productService.createProduct(productDto);
    }

    @GetMapping("/findAll")
    public Set<ProductDto> findAllProducts() {
        return this.productRepository.findAll().stream()
                .map(product -> {
                    ProductDto productDto = modelMapper.map(product, ProductDto.class);
                    productDto.setMediaUrl(product.getMedia().getMediaUrl());
                    productDto.setCategoryMediaUrl(product.getCategory().getMedia().getMediaUrl());
                    return productDto;
                })
                .collect(Collectors.toSet());
    }

    @DeleteMapping("/delete/{identifier}")
    public ResponseEntity<HttpResponse> deleteProduct(@PathVariable String identifier) throws ProductNotFoundException {
        return this.productService.deleteProduct(identifier);
    }
}