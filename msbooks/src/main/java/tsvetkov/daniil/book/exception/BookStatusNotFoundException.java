package tsvetkov.daniil.book.exception;

public class BookStatusNotFoundException extends RuntimeException {
    private static final String MESSAGE =  "Статус не найден";
    public BookStatusNotFoundException(String message) {
        super(message);
    }

    public BookStatusNotFoundException() {
        super(MESSAGE);
    }
}
