package tsvetkov.daniil.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BigFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBigFileException(BigFileException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(FileValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, String> handleFileValidationException(FileValidationException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(HighResolutionFileEsception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleHighResolutionFileEsception(HighResolutionFileEsception ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(UploadFileException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUploadFileException(UploadFileException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleInvalidTokenException(InvalidTokenException ex) {
        return Map.of("error", ex.getMessage());
    }
}
