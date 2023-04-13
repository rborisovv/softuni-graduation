package bg.rborisov.softunigraduation.web;

import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.ProductDto;
import bg.rborisov.softunigraduation.dto.VoucherDto;
import bg.rborisov.softunigraduation.exception.BasketNotFoundException;
import bg.rborisov.softunigraduation.exception.ProductNotFoundException;
import bg.rborisov.softunigraduation.exception.ProductSoldOutException;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.service.BasketService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

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

    @CacheEvict(value = "basket", key = "#principal.name")
    @PostMapping("/addToBasket")
    public ResponseEntity<HttpResponse> addToBasket(@RequestBody final String identifier, final Principal principal) throws ProductNotFoundException, UserNotFoundException, ProductSoldOutException {
        return this.basketService.addToBasket(identifier, principal);
    }

    @GetMapping("/userBasket")
    @Cacheable(value = "basket", key = "#principal.name")
    public Set<ProductDto> loadBasket(final Principal principal) throws UserNotFoundException {
        return this.basketService.loadBasket(principal);
    }

    @PostMapping("/removeFromBasket")
    @CacheEvict(value = "basket", key = "#principal.name")
    public ResponseEntity<HttpResponse> removeFromBasket(final @RequestBody String identifier, final Principal principal) throws ProductNotFoundException, UserNotFoundException {
        return this.basketService.removeFromBasket(identifier, principal);
    }

    @PostMapping("/updateBasketProduct")
    @CacheEvict(value = "basket", key = "#principal.name")
    public ResponseEntity<HttpResponse> updateBasketProduct(final Principal principal, final @RequestBody Map<String, String> productParams) throws ProductNotFoundException, UserNotFoundException, BasketNotFoundException {
        return this.basketService.updateBasketProduct(principal, productParams);
    }

    @PostMapping("/addVoucher")
    public ResponseEntity<VoucherDto> addVoucher(final @Valid @NotBlank @RequestBody String voucher) throws Exception {
        return this.basketService.addVoucherToBasket(voucher);
    }

    @GetMapping("/fetchVoucherIfPresent")
    public VoucherDto fetchVoucherIfPresent(final Principal principal) throws UserNotFoundException {
        return this.basketService.fetchVoucherIfPresent(principal);
    }
}