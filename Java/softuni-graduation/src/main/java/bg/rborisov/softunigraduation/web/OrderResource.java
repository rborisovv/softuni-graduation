package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
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
}