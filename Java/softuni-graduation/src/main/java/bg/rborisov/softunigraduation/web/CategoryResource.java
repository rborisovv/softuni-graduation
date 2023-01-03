package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.dto.CategoryDto;
import bg.rborisov.softunigraduation.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/category")
public class CategoryResource {

    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@RequestParam("name") String name,
                                                      @RequestParam("categoryIdentifier") String categoryIdentifier,
                                                      @RequestParam(name = "productNamePrefix", required = false) String productNamePrefix,
                                                      @RequestParam("media") MultipartFile media) {

        CategoryDto categoryDto = CategoryDto.builder()
                .name(name).categoryIdentifier(categoryIdentifier)
                .productNamePrefix(productNamePrefix)
                .media(media)
                .build();

        this.categoryService.createCategory(categoryDto);

        return null;
    }
}