package bg.rborisov.softunigraduation.httpFilter;

import bg.rborisov.softunigraduation.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

import static bg.rborisov.softunigraduation.constant.SecurityConstant.HTTP_OPTIONS_NAME;
import static bg.rborisov.softunigraduation.constant.SecurityConstant.TOKEN_PREFIX;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase(HTTP_OPTIONS_NAME)) {
            filterChain.doFilter(request, response);
        }

        String authorizationHeaders = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeaders == null || !authorizationHeaders.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
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
        filterChain.doFilter(request, response);
    }
}