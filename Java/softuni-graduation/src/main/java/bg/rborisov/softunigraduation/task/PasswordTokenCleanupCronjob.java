package bg.rborisov.softunigraduation.task;

import bg.rborisov.softunigraduation.dao.PasswordTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PasswordTokenCleanupCronjob {
    private final PasswordTokenRepository passwordTokenRepository;

    public PasswordTokenCleanupCronjob(PasswordTokenRepository passwordTokenRepository) {
        this.passwordTokenRepository = passwordTokenRepository;
    }

    @Scheduled(cron = "@hourly")
    public void expiredPasswordTokenCleanup() {
        this.passwordTokenRepository.deleteAll(this.passwordTokenRepository.findByExpireDateBefore(LocalDateTime.now()));
    }
}