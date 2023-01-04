package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.CategoryRepository;
import bg.rborisov.softunigraduation.dao.MediaRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.CategoryDto;
import bg.rborisov.softunigraduation.exception.CategoryWithIdentifierExists;
import bg.rborisov.softunigraduation.exception.MediaNotFoundException;
import bg.rborisov.softunigraduation.model.Category;
import bg.rborisov.softunigraduation.model.Media;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.CATEGORY_BY_IDENTIFIER_EXISTS;
import static bg.rborisov.softunigraduation.common.ExceptionMessages.MEDIA_NOT_FOUND;
import static bg.rborisov.softunigraduation.common.Messages.CATEGORY_CREATED;

@Service
public class CategoryService {

    private final MediaService mediaService;

    private final CategoryRepository categoryRepository;

    private final MediaRepository mediaRepository;

    private final ModelMapper modelMapper;

    public CategoryService(MediaService mediaService, CategoryRepository categoryRepository, MediaRepository mediaRepository, ModelMapper modelMapper) {
        this.mediaService = mediaService;
        this.categoryRepository = categoryRepository;
        this.mediaRepository = mediaRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<HttpResponse> createCategory(CategoryDto categoryDto) throws CategoryWithIdentifierExists, MediaNotFoundException {
        this.mediaService.saveFile(categoryDto.getMedia());

        String categoryIdentifier = categoryDto.getCategoryIdentifier();
        String identifier = Objects.requireNonNull(categoryDto.getMedia().getOriginalFilename()).replaceAll("\\s+", "-");
        Optional<Category> optionalCategory = this.categoryRepository.findCategoryByCategoryIdentifier(categoryIdentifier);

        String categoryName = categoryDto.getName();

        if (optionalCategory.isPresent()) {
            throw new CategoryWithIdentifierExists(String.format(CATEGORY_BY_IDENTIFIER_EXISTS, categoryDto.getCategoryIdentifier()));
        }

        Optional<Media> optionalMedia = this.mediaRepository.findMediaByIdentifier(identifier);

        if (optionalMedia.isEmpty()) {
            throw new MediaNotFoundException(MEDIA_NOT_FOUND);
        }

        Media media = optionalMedia.get();

        Category category = Category.builder()
                .name(categoryName)
                .categoryIdentifier(categoryDto.getCategoryIdentifier())
                .productNamePrefix(categoryDto.getProductNamePrefix())
                .mediaUrl(optionalMedia.get().getMediaUrl())
                .media(media).build();

        this.categoryRepository.save(category);

        HttpResponse response = new HttpResponse(HttpStatus.OK.value(),
                HttpStatus.OK, "", String.format(String.format(CATEGORY_CREATED, categoryName)));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public boolean isCategoryWithIdentifierPresent(String identifier) {
        return this.categoryRepository.findCategoryByCategoryIdentifier(identifier).isPresent();
    }

    public boolean isCategoryWithNamePresent(String name) {
        return this.categoryRepository.findCategoryByName(name).isPresent();
    }

    public Set<CategoryDto> loadAllCategories() {
        return this.categoryRepository.findAll()
                .stream().map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toSet());
    }

    public CategoryDto loadCategory(String identifier) {
        //TODO: throw exception if category not present
        Category category = this.categoryRepository.findCategoryByCategoryIdentifier(identifier).orElseThrow();
        return this.modelMapper.map(category, CategoryDto.class);
    }
}