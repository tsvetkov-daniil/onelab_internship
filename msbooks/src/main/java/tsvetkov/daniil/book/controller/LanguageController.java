package tsvetkov.daniil.book.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsvetkov.daniil.book.entity.Language;
import tsvetkov.daniil.book.service.LanguageService;

import java.util.List;
import java.util.Optional;




@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @PostMapping("/languages")
    public ResponseEntity<Language> createLanguage(@Valid @RequestBody Language language) {
        Language savedLanguage = languageService.save(language);
        return new ResponseEntity<>(savedLanguage, HttpStatus.CREATED);
    }

    @GetMapping("/languages/{id}")
    public ResponseEntity<Language> getLanguageById(@PathVariable Long id) {
        Language language = languageService.findById(id);
        return ResponseEntity.ok(language);
    }

    @GetMapping("/languages")
    public ResponseEntity<List<Language>> getAllLanguages() {
        List<Language> languages = languageService.findAll();
        return ResponseEntity.ok(languages);
    }

    @DeleteMapping("/languages/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        languageService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/languages/name/{name}")
    public ResponseEntity<Language> getLanguageByName(@PathVariable String name) {
        Optional<Language> language = languageService.findByName(name);
        return language.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
