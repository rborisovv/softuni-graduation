package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.ProductDto;
import bg.rborisov.softunigraduation.exception.ProductNotFoundException;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.service.UserService;
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

    @PostMapping("/addToFavourites")
    public ResponseEntity<HttpResponse> addToFavourites(@RequestBody String productIdentifier, Principal principal) throws ProductNotFoundException {
        return this.userService.addToFavourites(productIdentifier, principal);
    }

    @GetMapping("/favourites")
    public Set<ProductDto> loadFavouriteProducts(Principal principal) {
        return this.userService.loadFavouriteProducts(principal);
    }

    @PostMapping("/removeFromFavourites")
    public ResponseEntity<HttpResponse> removeProductFromFavourites(@RequestBody String identifier, Principal principal) throws UserNotFoundException, ProductNotFoundException {
        return this.userService.removeFromFavourites(identifier, principal);
    }

    @PostMapping("/addToBasket")
    public ResponseEntity<HttpResponse> addToCart(@RequestBody String identifier, Principal principal) throws ProductNotFoundException, UserNotFoundException {
        return this.userService.addToBasket(identifier, principal);
    }

    @GetMapping("/basket")
    public Set<ProductDto> loadBasket(Principal principal) throws UserNotFoundException {
        return this.userService.loadBasket(principal);
    }
}