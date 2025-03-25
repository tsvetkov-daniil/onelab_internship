package tsvetkov.daniil.book.exception;

public class BookNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Книга не найдена";

    public BookNotFoundException() {
        super(MESSAGE);
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}
