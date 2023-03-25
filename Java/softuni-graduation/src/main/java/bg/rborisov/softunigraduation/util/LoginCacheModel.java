package bg.rborisov.softunigraduation.util;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public final class LoginCacheModel<E> {
    private final E value;
    private final LocalDateTime timestamp;

    private static final Integer LOCK_IP_DURATION_EXPIRE_TIME = 15;

    public LoginCacheModel(E value) {
        this.value = value;
        this.timestamp = LocalDateTime.now().plusMinutes(LOCK_IP_DURATION_EXPIRE_TIME);
    }

    public E getValue() {
        return value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}