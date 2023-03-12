package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.service.BasketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/basket")
public class BasketResource {
    private final BasketService basketService;

    public BasketResource(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/canActivateCheckout")
    public boolean canActivateCheckout(final Principal principal) throws UserNotFoundException {
        return this.basketService.canActivateCheckout(principal);
    }
}