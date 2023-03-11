package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.CheckoutDto;
import bg.rborisov.softunigraduation.dto.ProductDto;
import bg.rborisov.softunigraduation.exception.BasketNotFoundException;
import bg.rborisov.softunigraduation.exception.ProductNotFoundException;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.Order;
import bg.rborisov.softunigraduation.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addToFavourites")
    public ResponseEntity<HttpResponse> addToFavourites(final @RequestBody String productIdentifier, final Principal principal) throws ProductNotFoundException {
        return this.userService.addToFavourites(productIdentifier, principal);
    }

    @GetMapping("/favourites")
    public Set<ProductDto> loadFavouriteProducts(Principal principal) {
        return this.userService.loadFavouriteProducts(principal);
    }

    @PostMapping("/removeFromFavourites")
    public ResponseEntity<HttpResponse> removeProductFromFavourites(final @RequestBody String identifier, final Principal principal) throws UserNotFoundException, ProductNotFoundException {
        return this.userService.removeFromFavourites(identifier, principal);
    }

    @PostMapping("/addToBasket")
    public ResponseEntity<HttpResponse> addToCart(@RequestBody final String identifier, final Principal principal) throws ProductNotFoundException, UserNotFoundException {
        return this.userService.addToBasket(identifier, principal);
    }

    @GetMapping("/basket")
    public Set<ProductDto> loadBasket(final Principal principal) throws UserNotFoundException {
        return this.userService.loadBasket(principal);
    }

    @PostMapping("/removeFromBasket")
    public ResponseEntity<HttpResponse> removeFromBasket(final @RequestBody String identifier, final Principal principal) throws ProductNotFoundException, UserNotFoundException {
        return this.userService.removeFromBasket(identifier, principal);
    }

    @PostMapping("/updateBasketProduct")
    public ResponseEntity<HttpResponse> updateBasketProduct(final Principal principal, final @RequestBody Map<String, String> productParams) throws ProductNotFoundException, UserNotFoundException, BasketNotFoundException {
        return this.userService.updateBasketProduct(principal, productParams);
    }

    @PostMapping("/submitCheckoutFlow")
    public void submitCheckoutFlow(final Principal principal, final @RequestBody CheckoutDto checkoutDto) throws UserNotFoundException {
        this.userService.submitCheckoutFlow(principal, checkoutDto);
    }

    @GetMapping("/fetchCheckoutData")
    public ResponseEntity<CheckoutDto> fetchCheckoutDataIfPresent(final Principal principal) throws UserNotFoundException {
        return this.userService.fetchCheckoutDataIfPresent(principal);
    }

    @PostMapping("/createOrder")
    public ResponseEntity<HttpResponse> createOrder(final Principal principal) {
        return this.userService.createOrder(principal);
    }
}