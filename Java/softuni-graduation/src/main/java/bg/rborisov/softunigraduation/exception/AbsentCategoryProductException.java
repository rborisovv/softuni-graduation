package bg.rborisov.softunigraduation.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AbsentCategoryProductException extends Exception {

    public AbsentCategoryProductException(String message) {
        super(message);
    }
}