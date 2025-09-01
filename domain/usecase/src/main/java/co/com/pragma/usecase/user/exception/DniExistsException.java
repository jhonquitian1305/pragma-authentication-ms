package co.com.pragma.usecase.user.exception;

public class DniExistsException extends RuntimeException {
    public DniExistsException(String message) {
        super(message);
    }
}
