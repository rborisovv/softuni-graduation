package bg.rborisov.softunigraduation.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VoucherExpiredException extends Exception {
    public VoucherExpiredException(final String message) {
        super(message);
    }
}