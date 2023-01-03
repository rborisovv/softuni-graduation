package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dto.CategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final MediaService mediaService;

    public CategoryService(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    public ResponseEntity<CategoryDto> createCategory(CategoryDto categoryDto) {
        this.mediaService.saveFile(categoryDto.getMedia());

        return null;
    }
}