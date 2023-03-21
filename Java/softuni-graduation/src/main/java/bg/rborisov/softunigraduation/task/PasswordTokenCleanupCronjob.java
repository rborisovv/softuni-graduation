package bg.rborisov.softunigraduation.task;

import bg.rborisov.softunigraduation.dao.PasswordTokenRepository;
import bg.rborisov.softunigraduation.enumeration.LoggerStatus;
import bg.rborisov.softunigraduation.model.PasswordToken;
import bg.rborisov.softunigraduation.util.logger.PasswordTokenLogger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import static bg.rborisov.softunigraduation.common.LogMessages.PASSWORD_TOKEN_CLEANUP;

@Component
public class PasswordTokenCleanupCronjob {
    private final PasswordTokenRepository passwordTokenRepository;

    public PasswordTokenCleanupCronjob(PasswordTokenRepository passwordTokenRepository) {
        this.passwordTokenRepository = passwordTokenRepository;
    }

    @Scheduled(cron = "@hourly")
    public void expiredPasswordTokenCleanup() throws IOException {
        int expiredPasswordTokensCount;
        Set<PasswordToken> expiredPasswordTokens = this.passwordTokenRepository.findByExpireDateBefore(LocalDateTime.now());
        expiredPasswordTokensCount = expiredPasswordTokens.size();

        this.passwordTokenRepository.deleteAll(expiredPasswordTokens);
        new PasswordTokenLogger().log(String.format(PASSWORD_TOKEN_CLEANUP,
                expiredPasswordTokensCount), LoggerStatus.INFO);
    }
}