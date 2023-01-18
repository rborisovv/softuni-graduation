package bg.rborisov.softunigraduation.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AbsentMediaOnProductException extends Exception {
    public AbsentMediaOnProductException(String message) {
        super(message);
    }
}