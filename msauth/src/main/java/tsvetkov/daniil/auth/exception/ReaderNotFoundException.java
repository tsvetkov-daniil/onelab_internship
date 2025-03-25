package tsvetkov.daniil.auth.exception;

public class ReaderNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Читатель не найден";

    public ReaderNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public ReaderNotFoundException(String message) {
        super(message);
    }
}
