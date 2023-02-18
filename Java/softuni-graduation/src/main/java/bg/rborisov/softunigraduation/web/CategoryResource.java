package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.CategoryDto;
import bg.rborisov.softunigraduation.dto.CategoryUpdateDto;
import bg.rborisov.softunigraduation.exception.*;
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
                                                       @RequestParam(name = "superCategory") String superCategory,
                                                       @RequestParam(value = "media", required = false) MultipartFile media,
                                                       @RequestParam(value = "pkOfFile", required = false) String pkOfFile)
            throws MediaNotFoundException, CategoryWithIdentifierExists, MediaByNameAlreadyExistsException, IOException, CategoryNotFoundException {

        @Valid CategoryDto categoryDto = CategoryDto.builder().name(name)
                .identifier(identifier)
                .superCategory(superCategory)
                .media(media).build();

        return this.categoryService.createCategory(categoryDto, pkOfFile);
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

    @GetMapping("/c/{identifier}")
    public CategoryDto loadCategoryWithBreadCrumb(@PathVariable String identifier) {
        return this.categoryService.loadCategoryWithBreadCrumb(identifier);
    }

    @GetMapping("/update/{identifier}")
    public CategoryUpdateDto categoryUpdateDto(@PathVariable String identifier) {
        return this.categoryService.loadCategoryToUpdate(identifier);
    }

    @PutMapping("/update")
    public HttpEntity<HttpResponse> updateCategory(@RequestParam("name") String name,
                                                   @RequestParam("oldName") String oldName,
                                                   @RequestParam("identifier") String identifier,
                                                   @RequestParam("oldCategoryIdentifier") String oldCategoryIdentifier,
                                                   @RequestParam(name = "superCategory") String superCategory,
                                                   @RequestParam(value = "media", required = false) MultipartFile media)
            throws CategoryNotFoundException, IOException, CategoryWithIdentifierExists, CategoryWithNameExists {

        CategoryUpdateDto categoryDto = CategoryUpdateDto.builder().name(name)
                .oldName(oldName)
                .identifier(identifier)
                .superCategory(superCategory)
                .oldIdentifier(oldCategoryIdentifier)
                .media(media).build();

        return this.categoryService.updateCategory(categoryDto);
    }

    @PostMapping("/filterByName")
    public Set<CategoryDto> filterCategoriesByName(@RequestBody String name) {
        return this.categoryService.filterByName(name);
    }

    @DeleteMapping("/delete/{identifier}")
    public ResponseEntity<HttpResponse> deleteCategory(@PathVariable String identifier) throws CategoryNotFoundException {
        return this.categoryService.deleteCategory(identifier);
    }
}