package bg.rborisov.softunigraduation.dao;

import bg.rborisov.softunigraduation.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findCategoryByIdentifier(String categoryIdentifier);

    Optional<Category> findCategoryByName(String name);
}