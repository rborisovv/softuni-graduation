package bg.rborisov.softunigraduation.dao;

import bg.rborisov.softunigraduation.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}