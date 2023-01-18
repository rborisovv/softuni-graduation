package bg.rborisov.softunigraduation.dao;

import bg.rborisov.softunigraduation.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    Optional<Media> findMediaByName(String name);

    Optional<Media> findMediaByPkOfFile(String pkOfFile);

    @Query("SELECT m FROM Media m WHERE m.name LIKE ?1")
    Set<Media> findMediaByNameLike(String name);
}