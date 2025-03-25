package tsvetkov.daniil.exception;

public class InvalidTokenException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Токен не действителен";

    public InvalidTokenException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
