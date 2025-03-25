package tsvetkov.daniil.book.exception;

public class LanguageNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Язык не найден";
    public LanguageNotFoundException(String message) {
        super(message);
    }

    public LanguageNotFoundException() {
        super(MESSAGE);
    }
}
