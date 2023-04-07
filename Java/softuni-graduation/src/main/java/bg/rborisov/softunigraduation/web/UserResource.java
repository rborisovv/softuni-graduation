package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.ProductDto;
import bg.rborisov.softunigraduation.dto.UserDto;
import bg.rborisov.softunigraduation.exception.ProductNotFoundException;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @CacheEvict("favourites")
    @PostMapping("/addToFavourites")
    public ResponseEntity<HttpResponse> addToFavourites(final @RequestBody String productIdentifier, final Principal principal) throws ProductNotFoundException {
        return this.userService.addToFavourites(productIdentifier, principal);
    }

    @Cacheable("favourites")
    @GetMapping("/favourites")
    public Set<ProductDto> loadFavouriteProducts(final Principal principal) {
        return this.userService.loadFavouriteProducts(principal);
    }

    @CacheEvict("favourites")
    @PostMapping("/removeFromFavourites")
    public ResponseEntity<HttpResponse> removeProductFromFavourites(final @RequestBody String identifier, final Principal principal) throws UserNotFoundException, ProductNotFoundException {
        return this.userService.removeFromFavourites(identifier, principal);
    }

    @PostMapping("/findUserByUsernameLike")
    public Set<UserDto> findUserByUsernameLike(final @Valid @NotBlank @RequestBody String username) {
        return this.userService.findUserByUsernameLike(username);
    }
}