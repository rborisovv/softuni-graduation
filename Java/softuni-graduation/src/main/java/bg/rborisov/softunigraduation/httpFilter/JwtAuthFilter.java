package bg.rborisov.softunigraduation.httpFilter;

import bg.rborisov.softunigraduation.exception.RsaKeyIntegrityViolationException;
import bg.rborisov.softunigraduation.util.JwtProvider;
import bg.rborisov.softunigraduation.util.RsaKeyIntegrityVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import static bg.rborisov.softunigraduation.common.JwtConstants.JWT_COOKIE_NAME;
import static bg.rborisov.softunigraduation.common.JwtConstants.TOKEN_PREFIX;
import static bg.rborisov.softunigraduation.constant.SecurityConstant.HTTP_OPTIONS_NAME;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final RsaKeyIntegrityVerifier rsaKeyIntegrityVerifier;

    public JwtAuthFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService, RsaKeyIntegrityVerifier rsaKeyIntegrityVerifier) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.rsaKeyIntegrityVerifier = rsaKeyIntegrityVerifier;
    }

    @Override
    @SuppressWarnings("nullness")
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            this.rsaKeyIntegrityVerifier.verifyRsaKeysIntegrity();
        } catch (NoSuchAlgorithmException | RsaKeyIntegrityViolationException e) {
            throw new ServletException(e);
        }

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

        if (requestWrapper.getMethod().equalsIgnoreCase(HTTP_OPTIONS_NAME)) {
            filterChain.doFilter(requestWrapper, response);
        }

        String authorizationHeaders = requestWrapper.getHeader(JWT_COOKIE_NAME);

        if (authorizationHeaders == null || !authorizationHeaders.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(requestWrapper, response);
            return;
        }

        String token = authorizationHeaders.substring(TOKEN_PREFIX.length());
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (jwtProvider.isTokenValid(token) && securityContext.getAuthentication() == null) {
            Set<GrantedAuthority> authorities = jwtProvider.getAuthorities(token);
            String username = jwtProvider.getSubject(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Authentication userPasswordToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), authorities);
            securityContext.setAuthentication(userPasswordToken);
        } else {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(requestWrapper, response);
    }
}