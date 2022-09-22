package bg.rborisov.softunigraduation.constant;

import org.springframework.http.HttpMethod;

public class SecurityConstant {
    public static final String[] PUBLIC_URLS = {"/user/login", "/user/register"};
    public static final String HTTP_OPTIONS_NAME = HttpMethod.OPTIONS.name();

    public static final int COOKIE_MAX_AGE = 432_000;
}