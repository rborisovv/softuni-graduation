package bg.rborisov.softunigraduation.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AbsentPasswordTokenException extends Exception {
    public AbsentPasswordTokenException(String message) {
        super(message);
    }
}