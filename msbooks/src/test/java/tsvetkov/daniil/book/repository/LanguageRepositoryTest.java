package tsvetkov.daniil.book.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.entity.Language;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataJpaTest
@Transactional
@Rollback
class LanguageRepositoryTest {

    private LanguageRepository languageRepository;

    @Autowired
    LanguageRepositoryTest(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @BeforeEach
    void setUp() {
        languageRepository.deleteAll();
    }

    @Test
    @DisplayName("Поиск существующего языка")
    void testFindByName() {
        final String langua = "English";
        assertThatCode(()->
                languageRepository.save(Language.builder().name(langua).build())).doesNotThrowAnyException();

        Optional<Language> foundLanguage = languageRepository.findByName(langua);

        assertThat(foundLanguage).isPresent()
                .hasValueSatisfying(lang -> assertThat(lang.getName()).isEqualTo(langua));
    }

    @Test
    @DisplayName("Поиск несуществующего языка")
    void testFindByNameNotFound() {
        Optional<Language> foundLanguage = languageRepository.findByName("French");

        assertThat(foundLanguage).isNotPresent();
    }
}
