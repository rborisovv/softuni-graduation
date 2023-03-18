package bg.rborisov.softunigraduation.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PasswordTokenExistsException extends Exception {
    public PasswordTokenExistsException(String message) {
        super(message);
    }
}