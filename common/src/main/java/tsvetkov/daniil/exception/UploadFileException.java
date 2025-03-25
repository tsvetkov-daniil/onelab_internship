package tsvetkov.daniil.exception;

public class UploadFileException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Ошибка загрузки файла";

    public UploadFileException() {
        super(DEFAULT_MESSAGE);
    }

    public UploadFileException(String message) {
        super(message);
    }
}
