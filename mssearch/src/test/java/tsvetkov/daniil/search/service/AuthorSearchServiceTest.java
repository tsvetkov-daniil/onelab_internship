package tsvetkov.daniil.search.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.AutoConfigureDataElasticsearch;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.search.dto.Author;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureDataElasticsearch
class AuthorSearchServiceTest {

    private final AuthorSearchService authorSearchService;

    @Autowired
    AuthorSearchServiceTest(AuthorSearchService authorSearchService) {
        this.authorSearchService = authorSearchService;
    }

    private Author testAuthor;

    @BeforeEach
    void setUp() {
        testAuthor = Author.builder()
                .index(1L)
                .firstName("Иван")
                .lastName("Иванов")
                .middleName("Иванович")
                .nickname("ivan123")
                .build();
        authorSearchService.deleteAll();
    }

    @Test
    @DisplayName("Сохранение автора")
    void testSave() {
        Author savedAuthor = authorSearchService.save(testAuthor);
        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.getFirstName()).isEqualTo("Иван");
    }

    @Test
    @DisplayName("Поиск автора по ID")
    void testFindById() {
        Author savedAuthor = authorSearchService.save(testAuthor);
        Optional<Author> foundAuthor = authorSearchService.findById(savedAuthor.getId());
        assertThat(foundAuthor).isPresent().contains(savedAuthor);
    }

    @Test
    @DisplayName("Поиск авторов по никнейму")
    void testFindByNickname() {
        authorSearchService.save(testAuthor);
        List<Author> authors = authorSearchService.findByNickname("ivan123");
        assertThat(authors).hasSize(1).contains(testAuthor);
    }

    @Test
    @DisplayName("Предложение имен авторов")
    void testSuggestAuthorNames() throws IOException {
        authorSearchService.save(testAuthor);
        List<String> suggestions = authorSearchService.suggestAuthorNames("iv", 5);
        assertThat(suggestions).contains("ivan123");
    }

    @Test
    @DisplayName("Удаление автора по ID")
    void testDeleteById() {
        Author savedAuthor = authorSearchService.save(testAuthor);
        authorSearchService.deleteById(savedAuthor.getId());
        Optional<Author> foundAuthor = authorSearchService.findById(savedAuthor.getId());
        assertThat(foundAuthor).isEmpty();
    }

    @Test
    @DisplayName("Удаление автора по индексу")
    void testDeleteByIndex() {
        authorSearchService.save(testAuthor);
        authorSearchService.deleteByIndex(1L);
        List<Author> authors = authorSearchService.findByNickname("ivan123");
        assertThat(authors).isEmpty();
    }
}