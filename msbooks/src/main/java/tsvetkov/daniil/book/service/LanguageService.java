package tsvetkov.daniil.book.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tsvetkov.daniil.book.entity.Language;
import tsvetkov.daniil.book.exception.LanguageNotFoundException;
import tsvetkov.daniil.book.repository.LanguageRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Validated
public class LanguageService {

    private final LanguageRepository languageRepository;

    @Transactional
    public Language save(@Valid Language language) {
        return languageRepository.save(language);
    }

    @Transactional(readOnly = true)
    public Language findById(Long id) {
        return languageRepository.findById(id)
                .orElseThrow(LanguageNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Set<Language> findAll(Integer pageNumber, Integer pageSize) {
        return new HashSet<>(languageRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent());
    }

    @Transactional
    public void deleteById(Long id) {
        findById(id);
        languageRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        languageRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public Optional<Language> findByName(String name) {
        return languageRepository.findByName(name);
    }
}
