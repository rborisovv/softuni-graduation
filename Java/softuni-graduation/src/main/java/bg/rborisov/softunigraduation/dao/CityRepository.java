package bg.rborisov.softunigraduation.dao;

import bg.rborisov.softunigraduation.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findCityByName(String name);

    Set<City> findByOrderByName();
}