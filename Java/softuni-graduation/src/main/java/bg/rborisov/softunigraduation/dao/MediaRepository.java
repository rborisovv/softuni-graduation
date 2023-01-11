package bg.rborisov.softunigraduation.dao;

import bg.rborisov.softunigraduation.enumeration.MediaTypeEnum;
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


    @Query("SELECT m FROM Media m WHERE m.name LIKE ?1 AND m.mediaSubject = bg.rborisov.softunigraduation.enumeration.MediaTypeEnum.CATEGORY")
    Set<Media> findMediaByNameAndCategoryMediaSubject(String name, MediaTypeEnum mediaSubject);

    @Query("SELECT m FROM Media m WHERE m.name LIKE ?1 AND m.mediaSubject = bg.rborisov.softunigraduation.enumeration.MediaTypeEnum.PRODUCT")
    Set<Media> findMediaByNameAndProductMediaSubject(String name, MediaTypeEnum mediaSubject);
}