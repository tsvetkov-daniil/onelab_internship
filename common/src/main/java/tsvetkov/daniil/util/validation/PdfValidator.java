package tsvetkov.daniil.util.validation;

import org.springframework.web.multipart.MultipartFile;

public class PdfValidator extends FileValidator {

    public PdfValidator(long maxFileSize) {
        super(maxFileSize, AllowedFileType.PDF);
    }

    @Override
    protected void checkSpecificRequirements(MultipartFile file) {
        checkContentType(file);
    }
}
