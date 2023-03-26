package bg.rborisov.softunigraduation.constant;

import org.springframework.http.HttpMethod;

public class SecurityConstant {
    public static final String[] PUBLIC_URLS = {"/auth/login", "/auth/csrf", "/auth/register", "/auth/email",
            "/auth/username", "/media/sys_master/h4f/{name}", "/auth/resetPassword", "/auth/changePassword",
            "/auth/hasActivePasswordRequest"};
    public static final String HTTP_OPTIONS_NAME = HttpMethod.OPTIONS.name();
    public static final int COOKIE_MAX_AGE = 1800;
    public static final String FORBIDDEN_MESSAGE = "You need to be logged in to access this page!";

    public static final String[] AUTH_ENDPOINTS = {"/auth/admin", "/auth/users", "/auth/orders"};
}