package tsvetkov.daniil.exception;

public class BigFileException extends FileValidationException {
    private static final String MESSEGE = "Размер файла больше допустимого";
    private static final String MESSEGE_MAX_SIZE = "Максимально допустимый размер файла: ";

    public BigFileException(String message) {
        super(message);
    }

    public BigFileException() {
        super(MESSEGE);
    }

    public BigFileException(Long maxSize) {
        super(MESSEGE_MAX_SIZE + maxSize);
    }
}
