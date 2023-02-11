package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.CategoryRepository;
import bg.rborisov.softunigraduation.dao.MediaRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.CategoryDto;
import bg.rborisov.softunigraduation.dto.CategoryUpdateDto;
import bg.rborisov.softunigraduation.exception.*;
import bg.rborisov.softunigraduation.model.Category;
import bg.rborisov.softunigraduation.model.Media;
import bg.rborisov.softunigraduation.util.AbstractMediaUrlBuilder;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.CATEGORY_NOT_FOUND;
import static bg.rborisov.softunigraduation.common.Messages.*;

@Service
@Transactional
public class CategoryService extends AbstractMediaUrlBuilder {
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

    public ResponseEntity<HttpResponse> createCategory(CategoryDto categoryDto, String pkOfFile) throws CategoryWithIdentifierExists, MediaNotFoundException, MediaByNameAlreadyExistsException, IOException, CategoryNotFoundException {
        String mediaName;
        Optional<Category> optionalCategory = this.categoryRepository.findCategoryByIdentifier(categoryDto.getIdentifier());
        String categoryName = categoryDto.getName();

        Optional<Media> optionalMedia;

        if (optionalCategory.isPresent()) {
            throw new CategoryWithIdentifierExists();
        }

        if (categoryDto.getMedia() != null) {
            //If new media is uploaded
            mediaName = Objects.requireNonNull(categoryDto.getMedia().getOriginalFilename()).replaceAll("\\s+", "-");
            optionalMedia = this.mediaRepository.findMediaByName(mediaName.substring(0, mediaName.length() - 4));

            if (optionalMedia.isPresent()) {
                throw new MediaByNameAlreadyExistsException();
            }

            this.mediaService.saveMedia(categoryDto.getMedia().getOriginalFilename(), categoryDto.getMedia());

            optionalMedia = this.mediaRepository.findMediaByName(categoryDto.getMedia().getOriginalFilename());

        } else {
            //If existing media is selected
            optionalMedia = this.mediaRepository.findMediaByPkOfFile(pkOfFile);

            if (optionalMedia.isEmpty()) {
                throw new MediaNotFoundException();
            }

            Media media = optionalMedia.get();
            mediaRepository.save(media);
        }

        Category category = Category.builder()
                .name(categoryName)
                .identifier(categoryDto.getIdentifier())
                .media(optionalMedia.get()).build();

        String superCategoryIdentifier = categoryDto.getSuperCategory();

        setSuperAndChildCategories(category, superCategoryIdentifier);

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
                .collect(Collectors.toCollection(LinkedHashSet::new));
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

        String superCategoryIdentifier = categoryDto.getSuperCategory();
        setSuperAndChildCategories(category, superCategoryIdentifier);
        //TODO: Fix the name of the upper method
        if (categoryDto.getMedia() != null) {
            String PK = RandomStringUtils.randomNumeric(15);

            String mediaUrl = super.constructMediaUrl(PK, categoryDto.getMedia());

            Media mediaEntity = new Media();
            mediaEntity.setPkOfFile(PK);
            mediaEntity.setFile(categoryDto.getMedia().getBytes());
            mediaEntity.setMediaUrl(mediaUrl);
            mediaEntity.setName(categoryDto.getMedia().getOriginalFilename());

            mediaRepository.save(mediaEntity);
            category.setMedia(null);
            category.setMedia(mediaEntity);
        }


        this.categoryRepository.save(category);


        HttpResponse response = new HttpResponse(HttpStatus.OK.value(),
                HttpStatus.OK, "", String.format(String.format(CATEGORY_UPDATED, categoryDto.getName())));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void setSuperAndChildCategories(Category category, String superCategoryIdentifier) throws CategoryNotFoundException {
        if (superCategoryIdentifier != null && !superCategoryIdentifier.equalsIgnoreCase("null")) {
            Category superCategory = this.categoryRepository.findCategoryByIdentifier(superCategoryIdentifier)
                    .orElseThrow(CategoryNotFoundException::new);

            category.setSuperCategory(superCategory);
        }
    }

    public ResponseEntity<HttpResponse> deleteCategory(String identifier) throws CategoryNotFoundException {
        Category category = this.categoryRepository.findCategoryByIdentifier(identifier)
                .orElseThrow(CategoryNotFoundException::new);

        this.categoryRepository.delete(category);

        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, "", String.format(CATEGORY_DELETED_SUCCESSFULLY, category.getName()));

        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }

    public Set<CategoryDto> filterByName(String name) {
        return this.categoryRepository.findCategoryByNameLike(name)
                .stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .sorted(Comparator.comparing(CategoryDto::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public CategoryDto loadCategoryWithBreadCrumb(String identifier) {
        Category category = this.categoryRepository.findCategoryByIdentifier(identifier).orElseThrow();
        Map<String, String> reversedBreadcrumb = new LinkedHashMap<>();
        Category parentCategory = category;

        while (parentCategory != null && !parentCategory.getName().equalsIgnoreCase("ROOT")) {
            reversedBreadcrumb.put(parentCategory.getName(), parentCategory.getIdentifier());
            parentCategory = parentCategory.getSuperCategory();
        }
        Map<String, String> breadcrumb = new LinkedHashMap<>();
        reverseBreadcrumb(reversedBreadcrumb, breadcrumb);
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        categoryDto.setBreadcrumb(breadcrumb);

        return categoryDto;
    }

    private static void reverseBreadcrumb(Map<String, String> reversedBreadcrumb, Map<String, String> breadcrumb) {
        List<String> reversedKeys = new ArrayList<>(reversedBreadcrumb.keySet());
        Collections.reverse(reversedKeys);

        for (String key : reversedKeys) {
            String value = reversedBreadcrumb.get(key);
            breadcrumb.put(key, value);
        }
    }

    public CategoryUpdateDto loadCategoryToUpdate(String identifier) {
        Category category = this.categoryRepository.findCategoryByIdentifier(identifier).orElseThrow();
        CategoryUpdateDto updatedCategory = this.modelMapper.map(category, CategoryUpdateDto.class);
        updatedCategory.setSuperCategory(category.getSuperCategory().getIdentifier());
        return updatedCategory;
    }
}