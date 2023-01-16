package bg.rborisov.softunigraduation.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MediaBoundToCategoryExistsException extends Exception {
    public MediaBoundToCategoryExistsException(String message) {
        super(message);
    }
}