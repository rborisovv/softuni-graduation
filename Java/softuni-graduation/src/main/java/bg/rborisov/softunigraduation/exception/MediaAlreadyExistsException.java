package bg.rborisov.softunigraduation.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MediaAlreadyExistsException extends Exception {
    public MediaAlreadyExistsException(String message) {
        super(message);
    }
}