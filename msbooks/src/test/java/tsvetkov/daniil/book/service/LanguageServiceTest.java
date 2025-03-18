package tsvetkov.daniil.book.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.dto.Language;
import tsvetkov.daniil.book.repository.LanguageRepository;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class LanguageServiceTest {

    private final LanguageService languageService;
    private final LanguageRepository languageRepository;

    @Autowired
    LanguageServiceTest(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
        this.languageService = new LanguageService(languageRepository);
    }

    private Language createValidLanguage() {
        return Language.builder()
                .name("Тестовый язык")
                .build();
    }

    @BeforeEach
    void setUp() {
        languageRepository.deleteAll();
    }

    @Test
    @DisplayName("Успешное сохранение нового языка")
    void testSave() {
        Language language = createValidLanguage();
        Language savedLanguage = languageService.save(language);

        Assertions.assertThat(savedLanguage.getId()).isNotNull();
        Assertions.assertThat(savedLanguage.getName()).isEqualTo("Тестовый язык");
    }

    @Test
    @DisplayName("Поиск языка по ID")
    void testFindById() {
        Language language = createValidLanguage();
        Language savedLanguage = languageService.save(language);

        Language foundLanguage = languageService.findById(savedLanguage.getId());

        Assertions.assertThat(foundLanguage).isEqualTo(savedLanguage);
    }

    @Test
    @DisplayName("Ошибка при поиске несуществующего языка по ID")
    void testFindByIdNotFound() {
        Assertions.assertThatThrownBy(() -> languageService.findById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Язык с id: 999 не найден");
    }

    @Test
    @DisplayName("Получение всех языков")
    void testFindAll() {
        languageService.save(createValidLanguage());
        languageService.save(Language.builder().name("Другой язык").build());

        List<Language> languages = languageService.findAll();

        Assertions.assertThat(languages).hasSize(2);
        Assertions.assertThat(languages).extracting("name").contains("Тестовый язык", "Другой язык");
    }

    @Test
    @DisplayName("Удаление языка по ID")
    void testDeleteById() {
        Language language = createValidLanguage();
        Language savedLanguage = languageService.save(language);

        languageService.deleteById(savedLanguage.getId());

        Assertions.assertThatThrownBy(() -> languageService.findById(savedLanguage.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Язык с id: " + savedLanguage.getId() + " не найден");
    }

    @Test
    @DisplayName("Удаление всех языков")
    void testDeleteAll() {
        languageService.save(createValidLanguage());
        languageService.save(Language.builder().name("Другой язык").build());

        languageService.deleteAll();

        Assertions.assertThat(languageService.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Поиск языка по имени")
    void testFindByName() {
        Language language = createValidLanguage();
        languageService.save(language);

        Optional<Language> foundLanguage = languageService.findByName("Тестовый язык");

        Assertions.assertThat(foundLanguage).isPresent();
        Assertions.assertThat(foundLanguage.get().getName()).isEqualTo("Тестовый язык");
    }

    @Test
    @DisplayName("Поиск несуществующего языка по имени")
    void testFindByNameNotFound() {
        Optional<Language> foundLanguage = languageService.findByName("Несуществующий язык");

        Assertions.assertThat(foundLanguage).isNotPresent();
    }
}
