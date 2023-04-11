package bg.rborisov.softunigraduation.filter;

import bg.rborisov.softunigraduation.httpFilter.JwtAuthEntryPoint;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JwtAuthEntryPointTest {

    private final JwtAuthEntryPoint jwtAuthEntryPoint = new JwtAuthEntryPoint();

    @Test
    public void testCommence() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException exception = new AuthenticationException("Forbidden") {
        };

        jwtAuthEntryPoint.commence(request, response, exception);

        assertEquals(403, response.getStatus());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(response.getContentAsByteArray());

        // Check the response body
        String expected = "{\"httpStatusCode\":403,\"httpStatus\":\"FORBIDDEN\",\"reason\":\"FORBIDDEN\",\"message\":\"Forbidden!\",\"notificationStatus\":null}";
        assertEquals(expected, outputStream.toString());
    }
}