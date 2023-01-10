package bg.rborisov.softunigraduation.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CategoryWithMediaExistsException extends Exception {
    public CategoryWithMediaExistsException(String message) {
        super(message);
    }
}