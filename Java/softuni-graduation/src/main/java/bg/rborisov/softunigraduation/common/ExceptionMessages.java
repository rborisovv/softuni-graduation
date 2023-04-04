package bg.rborisov.softunigraduation.common;

public class ExceptionMessages {
    public static final String USERNAME_OR_PASSWORD_INCORRECT = "The username or password is incorrect!";
    public static final String USERNAME_NOT_FOUND = "The username could not be found!";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified!";
    public static final String USER_WITH_USERNAME_OR_EMAIL_EXISTS = "A user with provided username or email already exists!";
    public static final String ACCOUNT_LOCKED = "Multiple incorrect login attempts!" +
            " Please wait %d minutes before trying again.";
    public static final String MEDIA_NOT_FOUND = "The media could not be found!";
    public static final String CATEGORY_BY_IDENTIFIER_EXISTS = "A Category with the provided Identifier already exists!";
    public static final String CATEGORY_BY_NAME_EXISTS = "A Category with the provided Name already exists!";
    public static final String CATEGORY_NOT_FOUND = "The category could not be found!";
    public static final String ABSENT_CATEGORY_BY_IDENTIFIER = "A category with the provided identifier could not be found!";
    public static final String MEDIA_BY_NAME_ALREADY_EXISTS = "A media with the provided name already exists!";
    public static final String PRODUCT_WITHOUT_CATEGORY = "The product does not have a category specified!";
    public static final String PRODUCT_WITHOUT_MEDIA = "The product does not have a media specified!";
    public static final String PRODUCT_SOLD_OUT = "The product is sold out!";
    public static final String OBJECT_CONTAINS_EXTERNAL_RELATIONS = "The object contains external relations with other entities. Please ensure those entities are removed before trying this again!";
    public static final String PRODUCT_COULD_NOT_BE_FOUND = "This product could not be found!";
    public static final String PASSWORD_TOKEN_NOT_FOUND = "No token has been generated for the provided user!";
    public static final String PASSWORD_TOKEN_EXISTS = "A password token is already generated for that user!";
    public static final String PASSWORD_TOKEN_EXPIRED = "The provided password token has expired!";
    public static final String VOUCHER_BY_NAME_PRESENT = "A voucher with the provided name is already present!";
    public static final String ABSENT_VOUCHER_BY_NAME = "A voucher with the provided name could not be found";
    public static final String VOUCHER_HAS_EXPIRED = "The voucher \"%s\" code has expired!";
    public static final String VOUCHER_CANNOT_BE_USED_BY_USER = "The voucher cannot be used!";
    public static final String USER_HAS_NO_BASKET = "No basket object found for user!";
}