package tsvetkov.daniil.book.exception;

public class ScoreNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Оценка не найдена";
    public ScoreNotFoundException(String message) {
        super(message);
    }

    public ScoreNotFoundException() {
        super(MESSAGE);
    }
}
