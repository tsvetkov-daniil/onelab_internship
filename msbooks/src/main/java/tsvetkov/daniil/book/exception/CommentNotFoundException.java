package tsvetkov.daniil.book.exception;

public class CommentNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Коментарий не найден";

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException() {
        super(MESSAGE);
    }
}
