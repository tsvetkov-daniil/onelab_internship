package tsvetkov.daniil.exception;

import java.io.IOException;

public class FileValidationException extends RuntimeException{


    private static final String DEFAULT_MESSAGE = "Файл не подходит по параметрам";

    public FileValidationException() {
        super(DEFAULT_MESSAGE);
    }

    public FileValidationException(String message) {
        super(message);
    }

    public FileValidationException(String message, IOException e) {
        super(message, e);
    }
}
