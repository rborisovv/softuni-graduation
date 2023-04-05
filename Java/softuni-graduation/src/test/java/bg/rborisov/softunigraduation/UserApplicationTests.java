package bg.rborisov.softunigraduation;

import bg.rborisov.softunigraduation.dao.ProductRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.exception.ProductNotFoundException;
import bg.rborisov.softunigraduation.model.Product;
import bg.rborisov.softunigraduation.model.User;
import bg.rborisov.softunigraduation.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Optional;

@SpringBootTest
public class UserApplicationTests {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private Principal principal;

    @Test
    public void shouldThrowExceptionIfProductNotFound() throws ProductNotFoundException {
        User user = new User();
        Product product = new Product();

        Mockito.when(this.userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        Mockito.when(this.productRepository.findProductByIdentifier("identifier")).thenReturn(Optional.of(product));

        final ResponseEntity<HttpResponse> response = this.userService.addToFavourites("identifier", this.principal);

        Assertions.assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());
    }
}