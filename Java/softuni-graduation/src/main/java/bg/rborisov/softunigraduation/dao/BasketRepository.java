package bg.rborisov.softunigraduation.dao;

import bg.rborisov.softunigraduation.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

}