package bg.rborisov.softunigraduation.dao;

import bg.rborisov.softunigraduation.model.Basket;
import bg.rborisov.softunigraduation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    Optional<Basket> findBasketByUser(User user);


    Set<Basket> findBasketByCreationDateBefore(LocalDateTime creationDate);
}