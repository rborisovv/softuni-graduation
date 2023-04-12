package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.dto.CheckoutDto;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.service.OrderService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/order")
public class OrderResource {
    private final OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/canActivateOrderFlow")
    public boolean canActivateOrderConfirmationFlow(final Principal principal) throws UserNotFoundException {
        return this.orderService.canActivateOrderConfirmationFlow(principal);
    }

    @PostMapping("/createOrder")
    @CacheEvict(value = "basket", key = "#principal.name")
    public void createOrder(final Principal principal) {
        this.orderService.createOrder(principal);
    }

    @PostMapping("/submitCheckoutFlow")
    public void submitCheckoutFlow(final Principal principal, final @RequestBody CheckoutDto checkoutDto) throws UserNotFoundException {
        this.orderService.submitCheckoutFlow(principal, checkoutDto);
    }

    @GetMapping("/fetchCheckoutData")
    public ResponseEntity<CheckoutDto> fetchCheckoutDataIfPresent(final Principal principal) throws UserNotFoundException {
        return this.orderService.fetchCheckoutDataIfPresent(principal);
    }
}