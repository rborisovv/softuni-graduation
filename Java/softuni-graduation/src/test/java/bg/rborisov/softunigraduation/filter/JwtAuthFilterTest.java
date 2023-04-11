package bg.rborisov.softunigraduation.filter;

import bg.rborisov.softunigraduation.httpFilter.JwtAuthFilter;
import bg.rborisov.softunigraduation.util.JwtProvider;
import bg.rborisov.softunigraduation.util.RsaKeyIntegrityVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;
import java.util.Set;

import static bg.rborisov.softunigraduation.common.JwtConstants.JWT_COOKIE_NAME;
import static bg.rborisov.softunigraduation.common.JwtConstants.TOKEN_PREFIX;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class JwtAuthFilterTest {
    @Test
    void doFilterInternal_ShouldSetSecurityContext_WhenValidToken() throws Exception {
        // Arrange
        JwtProvider jwtProvider = Mockito.mock(JwtProvider.class);
        UserDetailsService userDetailsService = Mockito.mock(UserDetailsService.class);
        RsaKeyIntegrityVerifier rsaKeyIntegrityVerifier = Mockito.mock(RsaKeyIntegrityVerifier.class);

        JwtAuthFilter filter = new JwtAuthFilter(jwtProvider, userDetailsService, rsaKeyIntegrityVerifier);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        String token = "Bearer valid_token";
        String username = "radi2000";
        String password = "password";
        Set<GrantedAuthority> authorities = Collections.emptySet();
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, password, authorities);

        Mockito.when(jwtProvider.isTokenValid(token)).thenReturn(true);
        Mockito.when(jwtProvider.getSubject(token)).thenReturn(username);
        Mockito.when(jwtProvider.getAuthorities(token)).thenReturn(authorities);
        Mockito.when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        request.addHeader(JWT_COOKIE_NAME, TOKEN_PREFIX + token);
        filter.doFilter(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert(authentication != null);
        assert(authentication.getPrincipal().equals(userDetails));
    }
}