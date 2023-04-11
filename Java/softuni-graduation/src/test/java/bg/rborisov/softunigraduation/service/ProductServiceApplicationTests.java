package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.CategoryRepository;
import bg.rborisov.softunigraduation.dao.MediaRepository;
import bg.rborisov.softunigraduation.dao.ProductRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.ProductDto;
import bg.rborisov.softunigraduation.exception.AbsentCategoryProductException;
import bg.rborisov.softunigraduation.exception.AbsentMediaOnProductException;
import bg.rborisov.softunigraduation.exception.ProductNotFoundException;
import bg.rborisov.softunigraduation.model.Category;
import bg.rborisov.softunigraduation.model.Media;
import bg.rborisov.softunigraduation.model.Product;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static bg.rborisov.softunigraduation.common.Messages.PRODUCT_CREATED_SUCCESSFULLY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceApplicationTests {
    @MockBean
    private MediaRepository mediaRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private Faker faker;
    private ProductDto productDto;

    @BeforeEach
    void init() {
        this.productDto = new ProductDto();
        this.productDto.setIdentifier(faker.commerce().material());
        this.productDto.setMedia(null);
        this.productDto.setName(faker.commerce().productName());
        this.productDto.setPrice(BigDecimal.TEN);
        this.productDto.setBestBefore(LocalDate.now().plusDays(10));
        this.productDto.setCategoryIdentifier(faker.idNumber().valid());
        this.productDto.setCategoryMediaUrl(faker.internet().image());
        this.productDto.setPkOfFile("pk");
        this.productDto.setQuantity(10);
        this.productDto.setShowBuyButton(true);
        this.productDto.setStockLevel(10);
    }

    @Test
    void shouldFailWhenNoMediaPresent() {
        this.productDto.setPkOfFile(null);
        assertThrows(AbsentMediaOnProductException.class, () -> this.productService.createProduct(this.productDto));
    }

    @Test
    void shouldFailWhenNoCategoryFoundForProduct() {
        this.productDto.setPkOfFile("pk");
        this.productDto.setCategoryIdentifier(null);

        assertThrows(AbsentCategoryProductException.class, () -> this.productService.createProduct(this.productDto));
    }

    @Test
    void shouldSuccessfullyCreateProduct() throws Exception {
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("media.png");
        Mockito.when(multipartFile.getBytes()).thenReturn(new byte[1]);
        this.productDto.setMedia(multipartFile);
        Category category = new Category();
        category.setProducts(new ArrayList<>());
        Mockito.when(this.categoryRepository.findCategoryByIdentifier(productDto.getCategoryIdentifier()))
                .thenReturn(Optional.of(category));

        ResponseEntity<HttpResponse> response = this.productService.createProduct(this.productDto);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(String.format(PRODUCT_CREATED_SUCCESSFULLY, this.productDto.getName()),
                Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void shouldSuccessfullyCreateProductWithExistingMedia() throws Exception {
        this.productDto.setPkOfFile("pk");
        Category category = new Category();
        category.setProducts(new ArrayList<>());
        Mockito.when(this.categoryRepository.findCategoryByIdentifier(productDto.getCategoryIdentifier()))
                .thenReturn(Optional.of(category));
        Mockito.when(this.mediaRepository.findMediaByPkOfFile("pk")).thenReturn(Optional.of(new Media()));

        ResponseEntity<HttpResponse> response = this.productService.createProduct(this.productDto);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(String.format(PRODUCT_CREATED_SUCCESSFULLY, this.productDto.getName()),
                Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void shouldSuccessfullyDeleteProduct() throws ProductNotFoundException {
        Mockito.when(this.productRepository.findProductByIdentifier("identifier")).thenReturn(Optional.of(new Product()));
        ResponseEntity<HttpResponse> response = this.productService.deleteProduct("identifier");
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    }
}