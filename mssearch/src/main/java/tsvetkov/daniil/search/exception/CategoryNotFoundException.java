package tsvetkov.daniil.search.exception;

public class CategoryNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Категория не найдена";
    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException() {
        super();
    }
}
