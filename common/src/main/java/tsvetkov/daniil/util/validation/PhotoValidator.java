package tsvetkov.daniil.util.validation;

import org.springframework.web.multipart.MultipartFile;
import tsvetkov.daniil.exception.FileValidationException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PhotoValidator extends FileValidator {

    private final int maxWidth;
    private final int maxHeight;

    public PhotoValidator(long maxFileSize, int maxWidth, int maxHeight) {
        super(maxFileSize, AllowedFileType.IMAGE);
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    @Override
    protected void checkSpecificRequirements(MultipartFile file) {
        checkContentType(file);

        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new FileValidationException("Невозможно прочитать изображение");
            }
            int width = image.getWidth();
            int height = image.getHeight();

            if (width > maxWidth || height > maxHeight) {
                throw new FileValidationException(
                        "Размеры изображения превышают максимальные: " + maxWidth + "x" + maxHeight);
            }
        } catch (IOException e) {
            throw new FileValidationException("Ошибка при проверке размеров изображения", e);
        }
    }
}
