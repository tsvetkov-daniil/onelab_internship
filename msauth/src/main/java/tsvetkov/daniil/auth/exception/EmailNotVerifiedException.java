package tsvetkov.daniil.auth.exception;

public class EmailNotVerifiedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Email не подтвержден";

    public EmailNotVerifiedException() {
        super(DEFAULT_MESSAGE);
    }

    public EmailNotVerifiedException(String message) {
        super(message);
    }
}
