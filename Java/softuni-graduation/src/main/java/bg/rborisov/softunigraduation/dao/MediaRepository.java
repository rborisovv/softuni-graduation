package bg.rborisov.softunigraduation.dao;

import bg.rborisov.softunigraduation.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    Optional<Media> findMediaByName(String name);

    Optional<Media> findMediaByIdentifier(String identifier);
}