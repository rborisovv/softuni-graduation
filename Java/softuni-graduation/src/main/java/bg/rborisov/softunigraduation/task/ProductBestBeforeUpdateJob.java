package bg.rborisov.softunigraduation.task;

import bg.rborisov.softunigraduation.dao.ProductRepository;
import bg.rborisov.softunigraduation.model.Product;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
public class ProductBestBeforeUpdateJob {
    private final ProductRepository productRepository;

    public ProductBestBeforeUpdateJob(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostConstruct
    @Transactional
    public void update() {
        Set<Product> products = this.productRepository.findAll().stream()
                .peek(p -> p.setBestBefore(generateRandomDate()))
                .collect(Collectors.toSet());

        this.productRepository.saveAll(products);
    }

    private LocalDate generateRandomDate() {
        int randomBestBefore = ThreadLocalRandom.current().nextInt(1, 15);
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusDays(randomBestBefore);
        long randomDay = startDate.toEpochDay() + ThreadLocalRandom.current().nextInt((int) (endDate.toEpochDay() - startDate.toEpochDay()));
        return LocalDate.ofEpochDay(randomDay);
    }
}