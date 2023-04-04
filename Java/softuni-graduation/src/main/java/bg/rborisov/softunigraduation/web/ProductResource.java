package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.dao.ProductRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.PageableData;
import bg.rborisov.softunigraduation.dto.ProductDto;
import bg.rborisov.softunigraduation.exception.*;
import bg.rborisov.softunigraduation.service.ProductService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

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
                                                      @RequestParam(value = "description", required = false) String description,
                                                      @RequestParam(value = "media", required = false) MultipartFile multipartFile,
                                                      @RequestParam(value = "stockLevel") Integer stockLevel,
                                                      @RequestParam(value = "showBuyButton") Boolean showBuyButton,
                                                      @RequestParam(value = "pkOfFile", required = false) String pkOfFile,
                                                      @RequestParam("categoryIdentifier") String categoryIdentifier) throws Exception {

        @Valid ProductDto productDto = ProductDto.builder()
                .name(name).identifier(identifier)
                .price(price).description(description).media(multipartFile)
                .stockLevel(stockLevel).showBuyButton(showBuyButton)
                .pkOfFile(pkOfFile).categoryIdentifier(categoryIdentifier).build();

        return this.productService.createProduct(productDto);
    }

    @PostMapping("/findAll")
    public Page<ProductDto> findAllProducts(final @Valid @RequestBody PageableData pageableData) {
        Pageable pageable = PageRequest.of(pageableData.getPageIndex(), pageableData.getPageSize());

        return this.productRepository.findAll(pageable)
                .map(product -> this.modelMapper.map(product, ProductDto.class));
    }

    @DeleteMapping("/delete/{identifier}")
    public ResponseEntity<HttpResponse> deleteProduct(@PathVariable String identifier) throws ProductNotFoundException {
        return this.productService.deleteProduct(identifier);
    }
}