package bg.rborisov.softunigraduation.util;

import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.TOKEN_CANNOT_BE_VERIFIED;
import static bg.rborisov.softunigraduation.common.JwtConstants.*;
import static java.util.Arrays.stream;

@Component
public class JwtProvider {
    private final UserDetailsService userDetailsService;

    private final UserService userService;
    private final RSAKeyProvider rsaKeyProvider;

    public JwtProvider(UserDetailsService userDetailsService, @Lazy UserService userService, RSAKeyProvider rsaKeyProvider) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.rsaKeyProvider = rsaKeyProvider;
    }

    public String generateToken() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String[] claims = getClaimsFromUser(username);
        try {
            Algorithm algorithm = Algorithm.RSA256(this.rsaKeyProvider);
            return JWT.create()
                    .withIssuer(TOKEN_ISSUER)
                    .withAudience(TOKEN_AUDIENCE)
                    .withIssuedAt(new Date())
                    .withSubject(username)
                    .withArrayClaim(AUTHORITIES, claims)
                    .withClaim(ROLE, getRole(username))
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException(exception.getMessage(), exception.getCause());
        }
    }

    private String getRole(String username) {
        try {
            return this.userService.findUserByUsername(username).getRole().getName();
        } catch (UserNotFoundException ex) {
            throw new JWTCreationException(ex.getMessage(), ex);
        }
    }

    public String getSubject(String token) {
        JWTVerifier jwtVerifier = getJwtVerifier();
        return jwtVerifier.verify(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        JWTVerifier jwtVerifier = getJwtVerifier();
        String subject = getSubject(token);
        return StringUtils.isNotBlank(subject) && !isTokenExpired(jwtVerifier, token);
    }

    public Set<GrantedAuthority> getAuthorities(String token) {
        String username = this.getSubject(token);
        String[] claims = getClaimsFromUser(username);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    private boolean isTokenExpired(JWTVerifier jwtVerifier, String token) {
        Date expiration = jwtVerifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    private JWTVerifier getJwtVerifier() {
        try {
            Algorithm algorithm = Algorithm.RSA256(this.rsaKeyProvider);
            return JWT.require(algorithm)
                    .withIssuer(TOKEN_ISSUER).build();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier jwtVerifier = getJwtVerifier();
        return jwtVerifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    private String[] getClaimsFromUser(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
    }
}