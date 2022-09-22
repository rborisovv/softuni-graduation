package bg.rborisov.softunigraduation.exception;

public class UserWithUsernameOrEmailExists extends Exception {

    public UserWithUsernameOrEmailExists(String message) {
        super(message);
    }
}