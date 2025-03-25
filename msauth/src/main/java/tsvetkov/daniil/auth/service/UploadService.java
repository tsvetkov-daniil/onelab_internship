package tsvetkov.daniil.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tsvetkov.daniil.exception.UploadFileException;
import tsvetkov.daniil.util.validation.PhotoValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UploadService {

    @Value("${file.photo.max-size:5242880}")
    private long photoMaxSize;
    @Value("${file.photo.max-width:1920}")
    private int photoMaxWidth;
    @Value("${file.photo.max-height:1080}")
    private int photoMaxHeight;

    private final String uploadDir = "/uploads/photos/";
    private final String urlPrefix = "/images/";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");


    @Transactional
    public String uploadUserPhoto(Long userId, MultipartFile file) {
        PhotoValidator photoValidator = new PhotoValidator(photoMaxSize, photoMaxWidth, photoMaxHeight);
        photoValidator.validate(file);

        try {
            Path uploadPath = Path.of(uploadDir);
            Files.createDirectories(uploadPath);

            String currentDate = LocalDate.now().format(DATE_FORMATTER);
            String fileName = currentDate + "_" + UUID.randomUUID() + getFileExtension(file);
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return urlPrefix + fileName;

        } catch (IOException e) {
            throw new UploadFileException();
        }
    }


    private String getFileExtension(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        return originalName != null && originalName.contains(".") ?
                originalName.substring(originalName.lastIndexOf(".")) : "";
    }
}
