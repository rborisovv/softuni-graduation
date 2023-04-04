package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.BasketRepository;
import bg.rborisov.softunigraduation.dao.ProductRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.dao.VoucherRepository;
import bg.rborisov.softunigraduation.domain.BasketHttpResponse;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.ProductDto;
import bg.rborisov.softunigraduation.dto.VoucherDto;
import bg.rborisov.softunigraduation.enumeration.NotificationStatus;
import bg.rborisov.softunigraduation.exception.*;
import bg.rborisov.softunigraduation.model.Basket;
import bg.rborisov.softunigraduation.model.Product;
import bg.rborisov.softunigraduation.model.User;
import bg.rborisov.softunigraduation.model.Voucher;
import bg.rborisov.softunigraduation.util.HttpResponseConstructor;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.VOUCHER_HAS_EXPIRED;
import static bg.rborisov.softunigraduation.common.Messages.*;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Service
@Transactional
public class BasketService {
    private final UserService userService;
    private final BasketRepository basketRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final VoucherRepository voucherRepository;

    public BasketService(UserService userService, BasketRepository basketRepository, final UserRepository userRepository, final ProductRepository productRepository, final ModelMapper modelMapper, final VoucherRepository voucherRepository) {
        this.userService = userService;
        this.basketRepository = basketRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.voucherRepository = voucherRepository;
    }

    public boolean canActivateCheckout(final Principal principal) throws UserNotFoundException {
        User user = this.userService.findUserByUsername(principal.getName());
        return this.basketRepository.findBasketByUser(user).isPresent();
    }

    public ResponseEntity<HttpResponse> addToBasket(final String identifier, final Principal principal) throws ProductNotFoundException, UserNotFoundException, ProductSoldOutException {
        if (identifier.isBlank()) {
            throw new IllegalArgumentException();
        }

        User user = this.userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);
        Product product = this.productRepository.findProductByIdentifier(identifier).orElseThrow(ProductNotFoundException::new);

        if (product.getStockLevel() <= 0) {
            throw new ProductSoldOutException();
        }

        if (user.getBasket() != null && user.getBasket().getProductMapping().containsKey(product)) {
            HttpResponse httpResponse = HttpResponseConstructor.construct(
                    OK, String.format(PRODUCT_ALREADY_ADDED_TO_BASKET, product.getName()),
                    NotificationStatus.INFO.name().toLowerCase(Locale.ROOT));

            BasketHttpResponse basketHttpResponse = this.modelMapper.map(httpResponse, BasketHttpResponse.class);
            basketHttpResponse.setBasketProducts(loadBasket(principal));
            return new ResponseEntity<>(basketHttpResponse, HttpStatus.OK);
        }

        Basket basket = user.getBasket();

        if (basket == null) {
            basket = new Basket();
            basket.setProductMapping(new HashMap<>());
            basket.setCreationDate(LocalDateTime.now());
        }

        basket.getProductMapping().put(product, 1);
        this.basketRepository.save(basket);
        user.setBasket(basket);

        HttpResponse httpResponse = HttpResponseConstructor.construct(
                OK, String.format(PRODUCT_SUCCESSFULLY_ADDED_TO_BASKET, product.getName()),
                NotificationStatus.SUCCESS.name().toLowerCase(Locale.ROOT)
        );

        BasketHttpResponse basketHttpResponse = this.modelMapper.map(httpResponse, BasketHttpResponse.class);
        basketHttpResponse.setBasketProducts(loadBasket(principal));
        return new ResponseEntity<>(basketHttpResponse, HttpStatus.OK);
    }

    public ResponseEntity<HttpResponse> updateBasketProduct(Principal principal, Map<String, String> productParams) throws ProductNotFoundException, UserNotFoundException, BasketNotFoundException {
        String identifier = productParams.get("identifier");
        Integer quantity = Integer.parseInt(productParams.get("quantity"));
        Product product = this.productRepository.findProductByIdentifier(identifier).orElseThrow(ProductNotFoundException::new);
        User user = this.userRepository.findByUsername(principal.getName()).orElseThrow(UserNotFoundException::new);
        Basket basket = this.basketRepository.findBasketByUser(user).orElseThrow(BasketNotFoundException::new);

        basket.getProductMapping().put(product, quantity);
        this.basketRepository.save(basket);

        Set<ProductDto> basketProducts = loadSortedUserBasketProducts(user);
        BasketHttpResponse basketHttpResponse = new BasketHttpResponse();
        basketHttpResponse.setHttpStatus(HttpStatus.OK);
        basketHttpResponse.setHttpStatusCode(HttpStatus.OK.value());
        basketHttpResponse.setBasketProducts(basketProducts);

        return new ResponseEntity<>(basketHttpResponse, HttpStatus.OK);
    }

    public ResponseEntity<HttpResponse> removeFromBasket(final String identifier, final Principal principal) throws ProductNotFoundException, UserNotFoundException {
        String username = principal.getName();

        Product product = this.productRepository.findProductByIdentifier(identifier).orElseThrow(ProductNotFoundException::new);
        User user = this.userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        Basket basket = user.getBasket();
        basket.getProductMapping().remove(product);

        if (basket.getProductMapping() == null || basket.getProductMapping().isEmpty()) {
            user.setBasket(null);
            this.basketRepository.delete(basket);
        }

        HttpResponse httpResponse = HttpResponseConstructor.construct(HttpStatus.OK,
                String.format(PRODUCT_REMOVED_FROM_BASKET, product.getName()), NotificationStatus.SUCCESS.name());
        BasketHttpResponse response = this.modelMapper.map(httpResponse, BasketHttpResponse.class);

        Set<ProductDto> userBasketProducts = loadSortedUserBasketProducts(user);
        response.setBasketProducts(userBasketProducts);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Set<ProductDto> loadBasket(Principal principal) throws UserNotFoundException {
        User user = this.userRepository.findByUsername(principal.getName())
                .orElseThrow(UserNotFoundException::new);

        return loadSortedUserBasketProducts(user);
    }

    private Set<ProductDto> loadSortedUserBasketProducts(User user) {
        if (user.getBasket() == null || user.getBasket().getProductMapping().isEmpty()) {
            return Collections.emptySet();
        }

        return user.getBasket().getProductMapping().entrySet()
                .stream().map((entry) -> {
                    ProductDto productDto = this.modelMapper.map(entry.getKey(), ProductDto.class);
                    productDto.setQuantity(entry.getValue());
                    return productDto;
                })
                .sorted(Comparator.comparing(ProductDto::getCategoryIdentifier).thenComparing(ProductDto::getPrice))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    public ResponseEntity<VoucherDto> addVoucherToBasket(final String voucherName) throws Exception {
        final Optional<Voucher> optionalVoucher = this.voucherRepository.findVoucherByName(voucherName);
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        final User user = this.userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        preValidationChecks(optionalVoucher, user);

        if (user.getBasket().getVoucher() != null) {
            throw new OneVoucherAlreadyAddedException();
        }

        user.getBasket().setVoucher(new Voucher());
        optionalVoucher.ifPresent(user.getBasket()::setVoucher);
        final Voucher voucher = optionalVoucher.orElseThrow(AbsentVoucherByNameException::new);
        final VoucherDto voucherDto = this.modelMapper.map(voucher, VoucherDto.class);

        return new ResponseEntity<>(voucherDto, HttpStatus.OK);
    }

    private void preValidationChecks(final Optional<Voucher> optionalVoucher, final User user) throws Exception {
        if (optionalVoucher.isEmpty()) {
            throw new AbsentVoucherByNameException();
        }

        final Voucher voucher = optionalVoucher.get();

        if (voucher.getExpirationDate().isBefore(LocalDate.now())) {
            throw new VoucherExpiredException(String.format(VOUCHER_HAS_EXPIRED, voucher.getName()));
        }

        if (voucher.getUser() != null) {
            if (voucher.getUser() != user) {
                throw new VoucherCannotBeUsedByUserException();
            }
        }

        if (voucher.getUser() != null && user.getBasket() == null) {
            throw new UserHasNoBasketException();
        }
    }

    public VoucherDto fetchVoucherIfPresent(final Principal principal) throws UserNotFoundException {
        User user = this.userService.findUserByUsername(principal.getName());
        if (user.getBasket() == null || user.getBasket().getVoucher() == null) {
            return null;
        }

        Voucher voucher = user.getBasket().getVoucher();
        return this.modelMapper.map(voucher, VoucherDto.class);
    }
}