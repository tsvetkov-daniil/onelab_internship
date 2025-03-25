package tsvetkov.daniil.auth.exception;

public class EmailNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Email не найден";

    public EmailNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public EmailNotFoundException(String message) {
        super(message);
    }
}
