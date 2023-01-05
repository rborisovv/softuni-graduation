package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.CategoryDto;
import bg.rborisov.softunigraduation.dto.CategoryUpdateDto;
import bg.rborisov.softunigraduation.exception.CategoryNotFoundException;
import bg.rborisov.softunigraduation.exception.CategoryWithIdentifierExists;
import bg.rborisov.softunigraduation.exception.CategoryWithNameExists;
import bg.rborisov.softunigraduation.exception.MediaNotFoundException;
import bg.rborisov.softunigraduation.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/category")
public class CategoryResource {

    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createCategory(@RequestParam("name") String name,
                                                       @RequestParam("identifier") String identifier,
                                                       @RequestParam(name = "productNamePrefix", required = false) String productNamePrefix,
                                                       @RequestParam("media") MultipartFile media) throws MediaNotFoundException, CategoryWithIdentifierExists {

        @Valid CategoryDto categoryDto = CategoryDto.builder().name(name)
                .identifier(identifier)
                .productNamePrefix(productNamePrefix)
                .media(media).build();

        return this.categoryService.createCategory(categoryDto);
    }

    @PostMapping("/identifier")
    public boolean checkCategoryByIdentifier(@RequestBody String identifier) {
        return this.categoryService.isCategoryWithIdentifierPresent(identifier);
    }

    @PostMapping("/name")
    public boolean checkCategoryByName(@RequestBody String name) {
        return this.categoryService.isCategoryWithNamePresent(name);


    }

    @GetMapping("/all")
    public Set<CategoryDto> loadCategories() {
        return this.categoryService.loadAllCategories();
    }

    @GetMapping("/{identifier}")
    public CategoryDto loadCategory(@PathVariable String identifier) {
        return this.categoryService.loadCategory(identifier);
    }

    @PutMapping("/update")
    public HttpEntity<HttpResponse> updateCategory(@RequestParam("name") String name,
                                                   @RequestParam("oldName") String oldName,
                                                   @RequestParam("identifier") String identifier,
                                                   @RequestParam("oldCategoryIdentifier") String oldCategoryIdentifier,
                                                   @RequestParam(name = "productNamePrefix", required = false) String productNamePrefix,
                                                   @RequestParam(value = "media", required = false) MultipartFile media)
            throws CategoryNotFoundException, IOException, CategoryWithIdentifierExists, CategoryWithNameExists {

        CategoryUpdateDto categoryDto = CategoryUpdateDto.builder().name(name)
                .oldName(oldName)
                .identifier(identifier)
                .productNamePrefix(productNamePrefix)
                .oldIdentifier(oldCategoryIdentifier)
                .media(media).build();

        return this.categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/delete/{identifier}")
    public ResponseEntity<HttpResponse> deleteCategory(@PathVariable String identifier) throws CategoryNotFoundException {
        return this.categoryService.deleteCategory(identifier);
    }
}