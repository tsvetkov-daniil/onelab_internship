package tsvetkov.daniil.search.aspect;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.search.entity.Author;
import tsvetkov.daniil.search.service.AuthorService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@EnableAspectJAutoProxy
class AuthorServiceTest {

    private final AuthorService authorService;

    @Autowired
    AuthorServiceTest(AuthorService authorService) {
        this.authorService = authorService;
    }

    private Author testAuthor;

    @BeforeEach
    void setUp() {
        testAuthor = Author.builder()
                .id(1L)
                .firstName("Иван")
                .lastName("Иванов")
                .middleName("Иванович")
                .nickname("ivan123")
                .build();
        authorService.deleteAll();
    }

    @Test
    @DisplayName("Сохранение автора")
    void testSave() {
        Author savedAuthor = authorService.save(testAuthor);
        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.getFirstName()).isEqualTo("Иван");
    }

    @Test
    @DisplayName("Поиск автора по ID")
    void testFindById() {
        Author savedAuthor = authorService.save(testAuthor);
        Optional<Author> foundAuthor = authorService.findById(savedAuthor.getId());
        System.err.println(foundAuthor);
        assertThat(foundAuthor).isPresent().contains(savedAuthor);
    }

    @Test
    @DisplayName("Поиск авторов по никнейму")
    void testFindByNickname() {
        authorService.save(testAuthor);
        List<Author> authors = authorService.findByNickname("ivan123");
        assertThat(authors).hasSize(1).contains(testAuthor);
    }

    @Test
    @DisplayName("Предложение имен авторов")
    void testSuggestAuthorName() throws IOException {
        authorService.save(testAuthor);
        List<String> suggestions = authorService.suggestAuthorName("ив", 5);
        assertThat(suggestions).contains("Иван Иванов Иванович");
    }

    @Test
    @DisplayName("Удаление автора по ID")
    void testDeleteById() {
        Author savedAuthor = authorService.save(testAuthor);
        authorService.deleteById(savedAuthor.getId());
        Optional<Author> foundAuthor = authorService.findById(savedAuthor.getId());
        assertThat(foundAuthor).isEmpty();
    }

    @Test
    @DisplayName("Удаление автора по индексу")
    void testDeleteByIndex() {
        authorService.save(testAuthor);
        authorService.deleteById(1L);
        List<Author> authors = authorService.findByNickname("ivan123");
        assertThat(authors).isEmpty();
    }
}