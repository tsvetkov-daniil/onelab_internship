package tsvetkov.daniil.review.exception;

public class ReviewNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Обзор не найдена";

    public ReviewNotFoundException() {
        super(MESSAGE);
    }

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
