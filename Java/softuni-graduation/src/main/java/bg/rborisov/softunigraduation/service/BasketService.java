package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.BasketRepository;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.model.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Slf4j
@Service
@Transactional
public class BasketService {
    private final UserService userService;
    private final BasketRepository basketRepository;

    public BasketService(UserService userService, BasketRepository basketRepository) {
        this.userService = userService;
        this.basketRepository = basketRepository;
    }

    public boolean canActivateCheckout(final Principal principal) throws UserNotFoundException {
        User user = this.userService.findUserByUsername(principal.getName());
        return this.basketRepository.findBasketByUser(user).isPresent();
    }
}