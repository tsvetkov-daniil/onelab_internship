package tsvetkov.daniil.auth.controler;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tsvetkov.daniil.auth.entity.Reader;
import tsvetkov.daniil.auth.service.ReaderService;
import tsvetkov.daniil.auth.service.UploadService;
import tsvetkov.daniil.auth.exception.ReaderNotFoundException;

import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserContoller {

    private final ReaderService readerService;
    private final UploadService uploadService;


    @PostMapping("/readers")
    public ResponseEntity<Reader> createReader(@Valid @RequestBody Reader reader){
        Reader savedReader = readerService.save(reader);
        return new ResponseEntity<>(savedReader, HttpStatus.CREATED);
    }

    @GetMapping("/readers/{id}")
    public ResponseEntity<Reader> getReaderById(@PathVariable Long id) {
        Reader reader = readerService.findById(id).orElseThrow(ReaderNotFoundException::new);
        return ResponseEntity.ok(reader);
    }

    @GetMapping("/readers/email/{email}")
    public ResponseEntity<Reader> getReaderByEmail(@PathVariable String email) {
        Reader reader = readerService.findByEmail(email).orElseThrow(ReaderNotFoundException::new);
        return ResponseEntity.ok(reader);
    }

    @GetMapping("/readers")
    public ResponseEntity<Set<Reader>> getAllReaders(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        Set<Reader> readers = readerService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok(readers);
    }

    @PutMapping("/readers/{id}")
    public ResponseEntity<Reader> updateReader(@PathVariable Long id, @Valid @RequestBody Reader reader) throws Exception {
        reader.setId(id);
        return ResponseEntity.ok(readerService.save(reader));
    }

    @DeleteMapping("/readers/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable Long id) throws Exception {
        readerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/readers/{userId}/upload-photo")
    public ResponseEntity<Reader> uploadPhoto(@PathVariable Long userId,
                                              @RequestParam("file") MultipartFile file) {
        Reader reader = readerService.setReaderPhoto(userId, file);
        return ResponseEntity.ok(reader);
    }

}