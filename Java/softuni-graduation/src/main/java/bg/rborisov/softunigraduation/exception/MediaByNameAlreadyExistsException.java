package bg.rborisov.softunigraduation.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MediaByNameAlreadyExistsException extends Exception {
    public MediaByNameAlreadyExistsException(String message) {
        super(message);
    }
}