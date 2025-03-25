package tsvetkov.daniil.book.exception;

// Общее исключение для некорректных операций с книгой
// например, попытка изменить статус напрямую через save
public class InvalidBookOperationException extends RuntimeException {
    private static final String MESSAGE = "Некорректная операция";

    public InvalidBookOperationException(String message) {
        super(message);
    }

    public InvalidBookOperationException() {
        super(MESSAGE);
    }
}
