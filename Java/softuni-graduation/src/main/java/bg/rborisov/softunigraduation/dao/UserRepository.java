package bg.rborisov.softunigraduation.dao;

import bg.rborisov.softunigraduation.model.Product;
import bg.rborisov.softunigraduation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findUserByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    @Query("SELECT u.favouriteProducts FROM User u where u.username = ?1")
    Set<Product> loadFavouriteProducts(String username);
}