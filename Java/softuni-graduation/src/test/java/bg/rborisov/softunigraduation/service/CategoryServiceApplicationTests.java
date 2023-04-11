package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.CategoryRepository;
import bg.rborisov.softunigraduation.dao.MediaRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.CategoryDto;
import bg.rborisov.softunigraduation.dto.CategoryUpdateDto;
import bg.rborisov.softunigraduation.exception.*;
import bg.rborisov.softunigraduation.model.Category;
import bg.rborisov.softunigraduation.model.Media;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static bg.rborisov.softunigraduation.common.Messages.CATEGORY_CREATED;
import static bg.rborisov.softunigraduation.common.Messages.CATEGORY_UPDATED;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CategoryServiceApplicationTests {
    @Autowired
    private CategoryService categoryService;
    @MockBean
    private MediaService mediaService;
    @Autowired
    private Faker faker;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private MediaRepository mediaRepository;
    private Media media;
    private CategoryDto categoryDto;

    @BeforeEach
    void init() {
        this.media = new Media();

        this.media.setFile(new byte[1]);
        this.media.setMediaUrl(this.faker.internet().image());
        this.media.setName("media");
        this.media.setPkOfFile(this.faker.idNumber().valid());
        this.media.setCreationTime(LocalDate.now());

        this.categoryDto = new CategoryDto();
        this.categoryDto.setIdentifier("category");
        this.categoryDto.setName("categoryName");
    }

    @Test
    void shouldFailWhenCategoryWithIdentifierExists() {
        Mockito.when(this.categoryRepository.findCategoryByIdentifier("category")).thenReturn(Optional.of(new Category()));
        assertThrows(CategoryWithIdentifierExists.class, () -> this.categoryService.createCategory(categoryDto, "pk"));
    }

    @Test
    void testShouldFailIfNoMediaByIdentifierFound() {
        this.categoryDto.setMedia(null);
        Mockito.when(this.mediaRepository.findMediaByPkOfFile("pk")).thenReturn(Optional.empty());
        assertThrows(MediaNotFoundException.class, () -> this.categoryService.createCategory(this.categoryDto, "pk"));
    }

    @Test
    void shouldSuccessfullyCreateCategory() throws Exception {
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        this.categoryDto.setMedia(multipartFile);
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("media.png");
        Mockito.when(this.categoryRepository.findCategoryByIdentifier("pk")).thenReturn(Optional.empty());
        Mockito.when(this.mediaService.constructMediaForEntity(multipartFile, "media.png")).thenReturn(Optional.of(this.media));

        ResponseEntity<HttpResponse> response = this.categoryService.createCategory(this.categoryDto, "pk");
        this.categoryDto.setMedia(null);
        Mockito.when(this.mediaRepository.findMediaByPkOfFile("pk")).thenReturn(Optional.of(this.media));
        ResponseEntity<HttpResponse> response2 = this.categoryService.createCategory(this.categoryDto, "pk");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(String.format(CATEGORY_CREATED, this.categoryDto.getName()),
                Objects.requireNonNull(response.getBody()).getMessage());

        assertEquals(HttpStatus.OK.value(), response2.getStatusCode().value());
        assertEquals(String.format(CATEGORY_CREATED, this.categoryDto.getName()),
                Objects.requireNonNull(response2.getBody()).getMessage());
    }

    @Test
    void shouldReturnIfCategoryWithIdentifierIsPresent() {
        Mockito.when(this.categoryRepository.findCategoryByIdentifier("identifier")).thenReturn(Optional.empty());
        assertFalse(this.categoryService.isCategoryWithIdentifierPresent("identifier"));
    }

    @Test
    void shouldReturnIfCategoryWithNameIsPresent() {
        Mockito.when(this.categoryRepository.findCategoryByName("name")).thenReturn(Optional.empty());
        assertFalse(this.categoryService.isCategoryWithNamePresent("name"));
    }

    @Test
    void shouldSuccessfullyLoadCategory() throws AbsentCategoryByIdentifier {
        Category category = new Category();
        category.setName("categoryName");
        category.setIdentifier("0000");
        Mockito.when(this.categoryRepository.findCategoryByIdentifier("identifier")).thenReturn(Optional.of(category));
        CategoryDto categoryDto = this.categoryService.loadCategory("identifier");

        assertEquals("categoryName", categoryDto.getName());
        assertEquals("0000", categoryDto.getIdentifier());
    }

    @Test
    void shouldFailWhenCategoryWithNameExists() {
        CategoryUpdateDto categoryUpdateDto = new CategoryUpdateDto();
        categoryUpdateDto.setName("newName");
        categoryUpdateDto.setOldName("oldName");
        categoryUpdateDto.setIdentifier("identifier");

        Mockito.when(this.categoryRepository.findCategoryByName("newName")).thenReturn(Optional.of(new Category()));
        assertThrows(CategoryWithNameExists.class, () -> this.categoryService.updateCategory(categoryUpdateDto));
    }

    @Test
    void shouldFailWhenCategoryWithIdentifierIsPresent() {
        CategoryUpdateDto categoryUpdateDto = new CategoryUpdateDto();
        categoryUpdateDto.setName("newName");
        categoryUpdateDto.setIdentifier("identifier");
        categoryUpdateDto.setOldIdentifier("oldIdentifier");

        Mockito.when(this.categoryRepository.findCategoryByIdentifier("identifier"))
                .thenReturn(Optional.of(new Category()));
    }

    @Test
    void shouldSuccessfullyUpdateCategory() throws IOException, CategoryWithNameExists, CategoryNotFoundException, CategoryWithIdentifierExists {
        CategoryUpdateDto categoryUpdateDto = new CategoryUpdateDto();
        categoryUpdateDto.setName("newName");
        categoryUpdateDto.setOldName("oldName");
        categoryUpdateDto.setIdentifier("identifier");
        categoryUpdateDto.setOldIdentifier("oldIdentifier");
        categoryUpdateDto.setSuperCategory("0000");

        Mockito.when(this.categoryRepository.findCategoryByName("newName")).thenReturn(Optional.empty());
        Mockito.when(this.categoryRepository.findCategoryByIdentifier("identifier")).thenReturn(Optional.empty());
        Mockito.when(this.categoryRepository.findCategoryByIdentifier("oldIdentifier")).thenReturn(Optional.of(new Category()));
        Mockito.when(this.categoryRepository.findCategoryByIdentifier("0000")).thenReturn(Optional.of(new Category()));

        ResponseEntity<HttpResponse> response = this.categoryService.updateCategory(categoryUpdateDto);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(String.format(CATEGORY_UPDATED, categoryUpdateDto.getName()),
                Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void ShouldSuccessfullyUpdateCategoryMedia() throws IOException, CategoryWithNameExists, CategoryNotFoundException, CategoryWithIdentifierExists {
        CategoryUpdateDto categoryUpdateDto = new CategoryUpdateDto();
        categoryUpdateDto.setName("newName");
        categoryUpdateDto.setOldName("oldName");
        categoryUpdateDto.setIdentifier("identifier");
        categoryUpdateDto.setOldIdentifier("oldIdentifier");
        categoryUpdateDto.setSuperCategory("0000");

        Mockito.when(this.categoryRepository.findCategoryByName("newName")).thenReturn(Optional.empty());
        Mockito.when(this.categoryRepository.findCategoryByIdentifier("identifier")).thenReturn(Optional.empty());
        Mockito.when(this.categoryRepository.findCategoryByIdentifier("oldIdentifier")).thenReturn(Optional.of(new Category()));
        Mockito.when(this.categoryRepository.findCategoryByIdentifier("0000")).thenReturn(Optional.of(new Category()));
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("media.png");
        Mockito.when(multipartFile.getBytes()).thenReturn(new byte[1]);
        categoryUpdateDto.setMedia(multipartFile);

        ResponseEntity<HttpResponse> response = this.categoryService.updateCategory(categoryUpdateDto);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(String.format(CATEGORY_UPDATED, categoryUpdateDto.getName()),
                Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void shouldSuccessfullyDeleteCategory() throws CategoryNotFoundException {
        Mockito.when(this.categoryRepository.findCategoryByIdentifier("identifier")).thenReturn(Optional.of(new Category()));

        ResponseEntity<HttpResponse> response = this.categoryService.deleteCategory("identifier");
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    }

    @Test
    void shouldReturnCategoryByNameLike() {
        assertEquals(0, this.categoryRepository.findCategoryByNameLike("name").size());
        Mockito.when(this.categoryRepository.findCategoryByNameLike("name")).thenReturn(new HashSet<>());
        Set<CategoryDto> response = this.categoryService.filterByName("name");
        assertEquals(0, response.size());
    }

    @Test
    void shouldReturnCategoryWithBreadcrumb() {
        Category category = new Category();
        category.setIdentifier("identifier");
        category.setName("name");
        category.setMedia(new Media());
        Category superCategory = new Category();
        superCategory.setIdentifier("ROOT");
        superCategory.setName("ROOT");
        category.setSuperCategory(superCategory);
        category.setProducts(new ArrayList<>());


        Mockito.when(this.categoryRepository.findCategoryByIdentifier("identifier")).thenReturn(Optional.of(category));
        CategoryDto categoryDto = this.categoryService.loadCategoryWithBreadCrumb("identifier");

        assertEquals("identifier", categoryDto.getIdentifier());
        assertEquals("name", categoryDto.getName());
    }

    @Test
    void shouldLoadCategoryToUpdate() {
        Category category = new Category();
        category.setIdentifier("identifier");
        category.setName("name");
        category.setMedia(new Media());
        Category superCategory = new Category();
        superCategory.setIdentifier("superIdentifier");
        category.setSuperCategory(superCategory);
        category.setProducts(new ArrayList<>());
        Mockito.when(this.categoryRepository.findCategoryByIdentifier("identifier")).thenReturn(Optional.of(category));

        CategoryUpdateDto response = this.categoryService.loadCategoryToUpdate("identifier");

        assertEquals("superIdentifier", response.getSuperCategory());
    }
}