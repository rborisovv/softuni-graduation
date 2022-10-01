package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.dao.CityRepository;
import bg.rborisov.softunigraduation.exception.ExceptionHandler;
import bg.rborisov.softunigraduation.model.City;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import static bg.rborisov.softunigraduation.constant.FileConstant.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping("/city")
public class CityResource extends ExceptionHandler {
    private final CityRepository cityRepository;

    public CityResource(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @GetMapping(value = "/{city}", produces = IMAGE_JPEG_VALUE)
    public byte[] city(@PathVariable String city) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + FORWARD_SLASH + "softuni-graduation" +
                FORWARD_SLASH + "Cities" + FORWARD_SLASH + city + MEDIA_EXTENSION_JPEG));
    }

    @GetMapping("/all")
    public Set<City> findAllCities() {
        return cityRepository.findByOrderByName();
    }
}