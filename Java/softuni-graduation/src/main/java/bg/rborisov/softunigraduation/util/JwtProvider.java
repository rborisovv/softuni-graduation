package bg.rborisov.softunigraduation.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.secret}")
    private String secret;

    public JwtProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String generateToken() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String[] claims = getClaimsFromUser(username);
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            return JWT.create()
                    .withIssuer(TOKEN_ISSUER)
                    .withAudience(TOKEN_AUDIENCE)
                    .withIssuedAt(new Date())
                    .withSubject(username)
                    .withArrayClaim(AUTHORITIES, claims)
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException(exception.getMessage(), exception.getCause());
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
        String[] claims = getClaimsFromUser(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    private boolean isTokenExpired(JWTVerifier jwtVerifier, String token) {
        Date expiration = jwtVerifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    private JWTVerifier getJwtVerifier() {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
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