package tsvetkov.daniil.book.exception;

public class CategoryNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Категория не найдена";

    public CategoryNotFoundException() {
        super(MESSAGE);
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
