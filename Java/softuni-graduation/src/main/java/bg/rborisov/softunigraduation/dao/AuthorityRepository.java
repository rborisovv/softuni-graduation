package bg.rborisov.softunigraduation.dao;

import bg.rborisov.softunigraduation.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}