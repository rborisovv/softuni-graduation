package bg.rborisov.softunigraduation.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CategoryWithNameExists extends Exception {

    public CategoryWithNameExists(String message) {
        super(message);
    }
}
