package tsvetkov.daniil.search.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.search.dto.Author;
import tsvetkov.daniil.search.dto.Book;
import tsvetkov.daniil.search.dto.Category;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BookSearchServiceTest {

    private final BookSearchService bookSearchService;

    @Autowired
    BookSearchServiceTest(BookSearchService bookSearchService) {
        this.bookSearchService = bookSearchService;
    }

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = Book.builder()
                .index(1L)
                .title("Тестовая книга")
                .description("Описание тестовой книги")
                .authors(Set.of(Author.builder()
                        .index(1L)
                        .firstName("Иван")
                        .lastName("Иванов")
                        .middleName("Иванович")
                        .nickname("ivan123")
                        .build()))
                .categories(Set.of(Category.builder()
                        .index(1L)
                        .name("Фантастика")
                        .build()))
                .publishDate(new Date())
                .price(29.99f)
                .build();
        bookSearchService.deleteAll();
        bookSearchService.save(testBook);
    }

    @Test
    @DisplayName("Сохранение книги")
    void testSave() {
        Book savedBook = bookSearchService.save(testBook);
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("Тестовая книга");
    }

    @Test
    @DisplayName("Поиск книги по ID")
    void testFindById() {
        Book savedBook = bookSearchService.save(testBook);
        Optional<Book> foundBook = bookSearchService.findById(savedBook.getId());
        assertThat(foundBook).isPresent().contains(savedBook);
    }

    @Test
    @DisplayName("Поиск книг по заголовку или описанию")
    void testSearchByTitleOrDescription() {
        List<Book> books = bookSearchService.searchByTitleOrDescription("Тестовая", 0, 10);
        assertThat(books).hasSize(1).contains(testBook);
    }

    @Test
    @DisplayName("Удаление книги по ID")
    void testDeleteById() {
        Book savedBook = bookSearchService.save(testBook);
        bookSearchService.deleteById(savedBook.getId());
        Optional<Book> foundBook = bookSearchService.findById(savedBook.getId());
        assertThat(foundBook).isEmpty();
    }

    @Test
    @DisplayName("Поиск книг по всем полям")
    void testSearchAcrossAllFields() throws IOException {
        List<Book> books = bookSearchService.searchAcrossAllFields("Иванов", 0, 10);
        assertThat(books).hasSize(1).contains(testBook);

        books = bookSearchService.searchAcrossAllFields("Фантастика", 0, 10);
        assertThat(books).hasSize(1).contains(testBook);

        books = bookSearchService.searchAcrossAllFields("Тестовая", 0, 10);
        assertThat(books).hasSize(1).contains(testBook);
    }

    @Test
    @DisplayName("Удаление книги по индексу")
    void testDeleteByIndex() {
        bookSearchService.deleteByIndex(1L);
        List<Book> books = bookSearchService.searchByTitleOrDescription("Тестовая", 0, 10);
        assertThat(books).isEmpty();
    }
}
