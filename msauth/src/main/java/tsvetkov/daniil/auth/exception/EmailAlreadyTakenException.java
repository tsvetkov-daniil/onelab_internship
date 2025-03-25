package tsvetkov.daniil.auth.exception;

public class EmailAlreadyTakenException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Email уже занят";

    public EmailAlreadyTakenException() {
        super(DEFAULT_MESSAGE);
    }

    public EmailAlreadyTakenException(String message) {
        super(message);
    }
}
