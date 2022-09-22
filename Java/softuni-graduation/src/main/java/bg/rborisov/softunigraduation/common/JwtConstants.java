package bg.rborisov.softunigraduation.common;

public class JwtConstants {
    public static final String TOKEN_ISSUER = "Radoslav Borisov";
    //TODO:
    public static final String TOKEN_AUDIENCE = "";
    public static final String AUTHORITIES = "Authorities";
    public static final long EXPIRATION_TIME = 432_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String JWT_COOKIE_NAME = "X-XSRF-TOKEN";
}