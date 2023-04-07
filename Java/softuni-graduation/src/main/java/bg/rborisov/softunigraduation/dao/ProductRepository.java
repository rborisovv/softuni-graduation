package bg.rborisov.softunigraduation.dao;

import bg.rborisov.softunigraduation.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByName(String name);

    Optional<Product> findProductByIdentifier(String identifier);
}