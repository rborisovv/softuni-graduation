package bg.rborisov.softunigraduation.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MediaNotFoundException extends Exception {
    public MediaNotFoundException(String message) {
        super(message);
    }
}