package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.dto.CheckoutDto;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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