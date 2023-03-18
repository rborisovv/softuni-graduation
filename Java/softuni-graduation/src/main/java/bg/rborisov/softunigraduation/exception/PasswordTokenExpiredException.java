package bg.rborisov.softunigraduation.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PasswordTokenExpiredException extends Exception {
    public PasswordTokenExpiredException(String message) {
        super(message);
    }
}