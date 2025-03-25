package tsvetkov.daniil.book.exception;

public class AuthorNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Автор не найден";
    public AuthorNotFoundException(String message) {
        super(message);
    }

    public AuthorNotFoundException() {
        super(MESSAGE);
    }
}
