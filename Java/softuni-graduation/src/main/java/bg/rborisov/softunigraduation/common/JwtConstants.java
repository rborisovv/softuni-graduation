package bg.rborisov.softunigraduation.common;

public class JwtConstants {
    public static final String TOKEN_ISSUER = "Radoslav Borisov";
    public static final String TOKEN_AUDIENCE = "";
    public static final String AUTHORITIES = "Authorities";
    public static final String ROLE = "role";
    public static final long EXPIRATION_TIME = 1_800_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_COOKIE_NAME = "JWT-TOKEN";
    public static final String JWT_ALGORITHM = "RSA";
}