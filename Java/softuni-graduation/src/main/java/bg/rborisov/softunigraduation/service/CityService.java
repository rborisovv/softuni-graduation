package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.CityRepository;
import bg.rborisov.softunigraduation.exception.CityNotFoundException;
import bg.rborisov.softunigraduation.model.City;
import org.springframework.stereotype.Service;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.CITY_NOT_FOUND;

@Service
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City findCityByName(String city) throws CityNotFoundException {
        return cityRepository.findCityByName(city).orElseThrow(() -> new CityNotFoundException(CITY_NOT_FOUND));
    }
}