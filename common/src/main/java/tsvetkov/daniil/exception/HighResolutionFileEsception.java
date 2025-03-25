package tsvetkov.daniil.exception;

public class HighResolutionFileEsception extends FileValidationException{
    private static final String MESSAGE = "Разрешение файла слишком большое";
    public HighResolutionFileEsception(String message) {
        super(message);
    }
    public HighResolutionFileEsception() {
        super(MESSAGE);
    }
}
