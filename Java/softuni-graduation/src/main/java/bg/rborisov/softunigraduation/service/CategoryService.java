package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.CategoryRepository;
import bg.rborisov.softunigraduation.dao.MediaRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.CategoryDto;
import bg.rborisov.softunigraduation.dto.CategoryUpdateDto;
import bg.rborisov.softunigraduation.exception.CategoryNotFoundException;
import bg.rborisov.softunigraduation.exception.CategoryWithIdentifierExists;
import bg.rborisov.softunigraduation.exception.CategoryWithNameExists;
import bg.rborisov.softunigraduation.exception.MediaNotFoundException;
import bg.rborisov.softunigraduation.model.Category;
import bg.rborisov.softunigraduation.model.Media;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.CATEGORY_NOT_FOUND;
import static bg.rborisov.softunigraduation.common.ExceptionMessages.MEDIA_NOT_FOUND;
import static bg.rborisov.softunigraduation.common.Messages.*;
import static bg.rborisov.softunigraduation.constant.FileConstant.MEDIA_LOCATION_PATH;

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

        String name = categoryDto.getName();
        String mediaName = Objects.requireNonNull(categoryDto.getMedia().getOriginalFilename()).replaceAll("\\s+", "-");
        Optional<Category> optionalCategory = this.categoryRepository.findCategoryByIdentifier(name);

        String categoryName = categoryDto.getName();

        if (optionalCategory.isPresent()) {
            throw new CategoryWithIdentifierExists();
        }

        Optional<Media> optionalMedia = this.mediaRepository.findMediaByName(mediaName);

        if (optionalMedia.isEmpty()) {
            throw new MediaNotFoundException(MEDIA_NOT_FOUND);
        }

        Media media = optionalMedia.get();

        Category category = Category.builder()
                .name(categoryName)
                .identifier(categoryDto.getIdentifier())
                .productNamePrefix(categoryDto.getProductNamePrefix())
                .media(media).build();

        this.categoryRepository.save(category);

        HttpResponse response = new HttpResponse(HttpStatus.OK.value(),
                HttpStatus.OK, "", String.format(String.format(CATEGORY_CREATED, categoryName)));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public boolean isCategoryWithIdentifierPresent(String identifier) {
        return this.categoryRepository.findCategoryByIdentifier(identifier).isPresent();
    }

    public boolean isCategoryWithNamePresent(String name) {
        return this.categoryRepository.findCategoryByName(name).isPresent();
    }

    public Set<CategoryDto> loadAllCategories() {
        return this.categoryRepository.findAll()
                .stream().map(category -> {
                    CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
                    categoryDto.setMediaUrl(category.getMedia().getMediaUrl());
                    return categoryDto;
                })
                .collect(Collectors.toSet());
    }

    public CategoryDto loadCategory(String identifier) {
        //TODO: throw exception if category not present
        Category category = this.categoryRepository.findCategoryByIdentifier(identifier).orElseThrow();
        return this.modelMapper.map(category, CategoryDto.class);
    }

    public ResponseEntity<HttpResponse> updateCategory(CategoryUpdateDto categoryDto) throws CategoryNotFoundException, IOException, CategoryWithIdentifierExists, CategoryWithNameExists {
        if (!categoryDto.getOldName().equals(categoryDto.getName())) {
            if (this.categoryRepository.findCategoryByName(categoryDto.getName()).isPresent()) {
                throw new CategoryWithNameExists();
            }
        }

        if (!categoryDto.getOldIdentifier().equals(categoryDto.getIdentifier())) {
            if (this.categoryRepository.findCategoryByIdentifier(categoryDto.getIdentifier()).isPresent()) {
                throw new CategoryWithIdentifierExists();
            }
        }


        Category category = this.categoryRepository.findCategoryByIdentifier(categoryDto.getOldIdentifier())
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND));

        category.setName(categoryDto.getName());
        category.setIdentifier(categoryDto.getIdentifier());
        category.setProductNamePrefix(categoryDto.getProductNamePrefix());
        if (categoryDto.getMedia() != null) {
            String PK = RandomStringUtils.randomNumeric(15);
            String mediaUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path(MEDIA_LOCATION_PATH.concat(PK)).toUriString();

            Media mediaEntity = category.getMedia();
            mediaEntity.setPkOfFile(PK);
            mediaEntity.setFile(categoryDto.getMedia().getBytes());
            mediaEntity.setMediaUrl(mediaUrl);
            mediaEntity.setName(categoryDto.getMedia().getOriginalFilename());

            mediaRepository.save(mediaEntity);
            category.setMedia(mediaEntity);
        }

        this.categoryRepository.save(category);


        HttpResponse response = new HttpResponse(HttpStatus.OK.value(),
                HttpStatus.OK, "", String.format(String.format(CATEGORY_UPDATED, categoryDto.getName())));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<HttpResponse> deleteCategory(String identifier) throws CategoryNotFoundException {
        Category category = this.categoryRepository.findCategoryByIdentifier(identifier)
                .orElseThrow(CategoryNotFoundException::new);

        this.categoryRepository.delete(category);

        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, "", String.format(CATEGORY_DELETED_SUCCESSFULLY, category.getName()));

        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }
}