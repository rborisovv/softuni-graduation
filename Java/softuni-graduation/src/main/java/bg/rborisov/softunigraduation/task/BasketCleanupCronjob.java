package bg.rborisov.softunigraduation.task;

import bg.rborisov.softunigraduation.dao.BasketRepository;
import bg.rborisov.softunigraduation.model.Basket;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class BasketCleanupCronjob {
    private final BasketRepository basketRepository;

    public BasketCleanupCronjob(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Scheduled(cron = "0 0 * * 0 */2 *")
    public void expiredBasketsCleanup() {
        Set<Basket> expiredBaskets = this.basketRepository.findBasketByCreationDateBefore(LocalDateTime.now());
        this.basketRepository.deleteAll(expiredBaskets);
    }
}
