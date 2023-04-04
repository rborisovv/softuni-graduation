package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.CategoryRepository;
import bg.rborisov.softunigraduation.dao.MediaRepository;
import bg.rborisov.softunigraduation.dao.ProductRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.ProductDto;
import bg.rborisov.softunigraduation.exception.*;
import bg.rborisov.softunigraduation.model.Category;
import bg.rborisov.softunigraduation.model.Media;
import bg.rborisov.softunigraduation.model.Product;
import bg.rborisov.softunigraduation.util.validators.ImageValidator;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static bg.rborisov.softunigraduation.common.Messages.PRODUCT_CREATED_SUCCESSFULLY;
import static bg.rborisov.softunigraduation.common.Messages.PRODUCT_DELETED_SUCCESSFULLY;

@Service
@Transactional
public class ProductService {

    @Resource
    private MediaService mediaService;
    @Resource
    private MediaRepository mediaRepository;
    @Resource
    private CategoryRepository categoryRepository;
    @Resource
    private ProductRepository productRepository;
    @Resource
    private ModelMapper modelMapper;

    public ResponseEntity<HttpResponse> createProduct(ProductDto productDto) throws Exception {

        String categoryIdentifier = productDto.getCategoryIdentifier();
        @ImageValidator MultipartFile multipartFile = productDto.getMedia();
        String pkOfFile = productDto.getPkOfFile();
        Optional<Media> optionalMedia;

        if (multipartFile == null && (pkOfFile == null || StringUtils.isBlank(pkOfFile))) {
            throw new AbsentMediaOnProductException();
        }

        if (categoryIdentifier == null || StringUtils.isBlank(categoryIdentifier)) {
            throw new AbsentCategoryProductException();
        }

        assert multipartFile != null;
        if (productDto.getMedia() != null) {
            String mediaName = Objects.requireNonNull(multipartFile.getOriginalFilename());
            optionalMedia = this.mediaService.constructMediaForEntity(productDto.getMedia(), mediaName);

        } else {
            //If existing media is selected
            optionalMedia = this.mediaRepository.findMediaByPkOfFile(pkOfFile);
            if (optionalMedia.isEmpty()) {
                throw new MediaNotFoundException();
            }

            Media media = optionalMedia.get();
            mediaRepository.save(media);
        }

        Category category = categoryRepository.findCategoryByIdentifier(productDto.getCategoryIdentifier())
                .orElseThrow(CategoryNotFoundException::new);


        Product product = this.modelMapper.map(productDto, Product.class);

        optionalMedia.ifPresent(product::setMedia);
        product.setCategory(category);
        product.setCreationTime(LocalDate.now());

        this.productRepository.save(product);

        HttpResponse httpResponse = HttpResponse.builder()
                .httpStatusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .reason("")
                .message(String.format(PRODUCT_CREATED_SUCCESSFULLY, productDto.getName()))
                .build();

        category.getProducts().add(product);

        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }

    public ResponseEntity<HttpResponse> deleteProduct(String identifier) throws ProductNotFoundException {
        Product product = this.productRepository.findProductByIdentifier(identifier).orElseThrow(ProductNotFoundException::new);
        this.productRepository.delete(product);

        HttpResponse httpResponse = HttpResponse.builder()
                .httpStatusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .reason("")
                .message(String.format(PRODUCT_DELETED_SUCCESSFULLY, product.getName()))
                .build();

        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }
}