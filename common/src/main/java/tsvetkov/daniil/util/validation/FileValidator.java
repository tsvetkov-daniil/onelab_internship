package tsvetkov.daniil.util.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import tsvetkov.daniil.exception.FileValidationException;

@RequiredArgsConstructor
public abstract class FileValidator {

    protected final long maxFileSizeMb;
    protected final AllowedFileType allowedType;


    public void validate(MultipartFile file) {
        checkBasicRequirements(file);
        checkFileSize(file);
        checkSpecificRequirements(file);
    }

    private void checkBasicRequirements(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileValidationException("Файл пустой или отсутствует");
        }
    }

    private void checkFileSize(MultipartFile file) {
        if (file.getSize() > maxFileSizeMb) {
            throw new FileValidationException(
                    "Размер файла превышает " + (maxFileSizeMb / (1024 * 1024)) + " MB");
        }
    }

    protected void checkContentType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith(allowedType.getMimeTypePrefix())) {
            throw new FileValidationException("Файл не соответствует типу: " + allowedType.name());
        }
    }

    protected abstract void checkSpecificRequirements(MultipartFile file);
}