package tsvetkov.daniil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.dto.Language;
import tsvetkov.daniil.repository.LanguageRepository;

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
    public Language create(Language language) {
        return languageRepository.save(language);
    }

    public Optional<Language> findById(Long id) {
        return languageRepository.findById(id);
    }

    public List<Language> findAll() {
        return languageRepository.findAll();
    }

    @Transactional
    public Language update(Long id, Language updatedLanguage) {
        Language existingLanguage = languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Язык не найден"));

        existingLanguage.setName(updatedLanguage.getName());
        return languageRepository.save(existingLanguage);
    }

    @Transactional
    public void delete(Long id) {
        languageRepository.deleteById(id);
    }

    public Optional<Language> findByName(String name) {
        return languageRepository.findByName(name);
    }
}
