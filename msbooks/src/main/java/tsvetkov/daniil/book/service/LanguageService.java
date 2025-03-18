package tsvetkov.daniil.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.dto.Language;
import tsvetkov.daniil.book.repository.LanguageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {

    private final LanguageRepository languageRepository;

    @Autowired
    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Transactional
    public Language save(Language language) {
        return languageRepository.save(language);
    }

    public Language findById(Long id) {
        return languageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Язык с id: " + id + " не найден"));
    }

    public List<Language> findAll() {
        return languageRepository.findAll();
    }


    @Transactional
    public void deleteById(Long id) {
        languageRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        languageRepository.deleteAll();
    }

    public Optional<Language> findByName(String name) {
        return languageRepository.findByName(name);
    }
}
