package bg.rborisov.softunigraduation.util.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import static bg.rborisov.softunigraduation.common.Messages.IMAGES_CONSTRAINT_MESSAGE;
import static bg.rborisov.softunigraduation.constant.FileConstant.*;

public final class ImageFileValidator implements ConstraintValidator<ImageValidator, MultipartFile> {

    @Override
    public void initialize(ImageValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        boolean result = true;

        String contentType = multipartFile.getContentType();
        assert contentType != null;
        if (!isSupportedContentType(contentType)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(IMAGES_CONSTRAINT_MESSAGE)
                    .addConstraintViolation();

            result = false;
        }

        return result;
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equalsIgnoreCase(MEDIA_EXTENSION_JPEG)
                || contentType.equalsIgnoreCase(MEDIA_EXTENSION_JPG)
                || contentType.equals(MEDIA_EXTENSION_PNG);
    }
}