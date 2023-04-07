package bg.rborisov.softunigraduation.dao;

import bg.rborisov.softunigraduation.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findCategoryByIdentifier(String categoryIdentifier);

    Optional<Category> findCategoryByName(String name);

    @Query("SELECT c FROM Category as c WHERE c.identifier LIKE ?1 OR c.name like ?1")
    Set<Category> findCategoryByNameLike(String name);
}