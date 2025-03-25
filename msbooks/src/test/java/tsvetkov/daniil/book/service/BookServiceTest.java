package tsvetkov.daniil.book.service;

import org.assertj.core.api.Assertions;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.entity.Book;
import tsvetkov.daniil.book.entity.BookStatus;
import tsvetkov.daniil.book.entity.Category;
import tsvetkov.daniil.book.entity.Language;
import tsvetkov.daniil.book.repository.BookRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@Transactional
class BookServiceTest {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final BookStatusService bookStatusService;
    private final LanguageService languageService;
    private final EventProducer bookEventProducer;

    private Category defaultCategory;
    private BookStatus defaultStatus;
    private Language defaultLanguage;

    @Autowired
    BookServiceTest(BookService bookService, BookRepository bookRepository, CategoryService categoryService, BookStatusService bookStatusService, LanguageService languageService, EventProducer bookEventProducer) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
        this.bookStatusService = bookStatusService;
        this.languageService = languageService;
        this.bookEventProducer = bookEventProducer;
    }

    private Book createValidBook() {
        Set<Long> authors = new HashSet<>();
        authors.add(1L);

        Set<Category> categories = new HashSet<>();
        categories.add(defaultCategory);

        return Book.builder()
                .title("Тестовая книга")
                .description("Описание книги")
                .publishDate(new Date())
                .coverUrl("http://example.com/cover.jpg")
                .price(100.0f)
                .language(defaultLanguage)
                .status(defaultStatus)
                .authors(authors)
                .categories(categories)
                .build();
    }

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();

        defaultStatus = BookStatus.builder()
                .title("Доступно")
                .description("Книга доступна")
                .build();
        bookStatusService.save(defaultStatus);

        defaultCategory = Category.builder()
                .name("Фантастика")
                .description("Жанр фантастики")
                .build();
        categoryService.save(defaultCategory);

        defaultLanguage = Language.builder()
                .name("Русский")
                .build();
        languageService.save(defaultLanguage);
    }

    @Test
    @DisplayName("Успешное сохранение новой книги")
    void testSaveNewBook() {
        Book book = createValidBook();
        Book savedBook = bookService.save(book);

        Assertions.assertThat(savedBook.getId()).isNotNull();
        Assertions.assertThat(savedBook.getTitle()).isEqualTo("Тестовая книга");
        Assertions.assertThat(savedBook.getPublishDate()).isNotNull();
//        Assertions.assertThat(savedBook.getStatus()).isEqualTo(defaultStatus);
    }

    @Test
    @DisplayName("Ошибка при сохранении книги с несуществующей категорией")
    void testSaveBookWithInvalidCategory() {
        Book book = createValidBook();
        book.getCategories().clear();
        book.getCategories().add(Category.builder().id(999L).name("Несуществующая").build());

        Assertions.assertThatThrownBy(() -> bookService.save(book))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Успешное обновление книги без изменения статуса и даты")
    void testUpdateBook() {
        Book book = createValidBook();
        Book savedBook = bookService.save(book);

        savedBook.setTitle("Обновлённая книга");
        Book updatedBook = bookService.save(savedBook);

        Assertions.assertThat(updatedBook.getTitle()).isEqualTo("Обновлённая книга");
        Assertions.assertThat(updatedBook.getPublishDate()).isEqualTo(savedBook.getPublishDate());
        Assertions.assertThat(updatedBook.getStatus()).isEqualTo(savedBook.getStatus());
    }

    @Test
    @DisplayName("Поиск книги по ID")
    void testFindById() {
        Book book = createValidBook();
        Book savedBook = bookService.save(book);

        Book foundBook = bookService.findById(savedBook.getId());

        Assertions.assertThat(foundBook).isEqualTo(savedBook);
    }

    @Test
    @DisplayName("Ошибка при поиске несуществующей книги")
    void testFindByIdNotFound() {
        Assertions.assertThatThrownBy(() -> bookService.findById(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Получение списка книг с пагинацией")
    void testFindAll() {
        bookService.save(createValidBook());
        bookService.save(createValidBook());

        Set<Book> books = bookService.findAll(0, 1);

        Assertions.assertThat(books).hasSize(1);
    }

    @Test
    @DisplayName("Удаление книги по ID")
    void testDeleteById() {
        Book book = createValidBook();
        Book savedBook = bookService.save(book);

        bookService.deleteById(savedBook.getId());

        Assertions.assertThatThrownBy(() -> bookService.findById(savedBook.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Книга не найдена");
    }

    @Test
    @DisplayName("Добавление категории к книге")
    void testAddCategoryToBook() {
        Book book = createValidBook();
        Book savedBook = bookService.save(book);
        Category newCategory = Category.builder().name("Детектив").description("Жанр детективов").build();
        categoryService.save(newCategory);

        Book updatedBook = bookService.addCategoryToBook(savedBook.getId(), newCategory.getId());

        Assertions.assertThat(updatedBook.getCategories()).hasSize(2);
        Assertions.assertThat(updatedBook.getCategories()).extracting("name").contains("Детектив");
    }

    @Test
    @DisplayName("Изменение статуса книги")
    void testChangeStatus() {
        Book book = createValidBook();
        Book savedBook = bookService.save(book);
        BookStatus newStatus = BookStatus.builder().title("Недоступно").description("Книга недоступна").build();
        bookStatusService.save(newStatus);

        Book updatedBook = bookService.changeStatus(savedBook.getId(), newStatus.getId());

        Assertions.assertThat(updatedBook.getStatus().getTitle()).isEqualTo("Недоступно");
    }

    @Test
    @DisplayName("Удаление категории из книги")
    void testDeleteCategoryFromBook() {
        Book book = createValidBook();
        Book savedBook = bookService.save(book);

        Book updatedBook = bookService.deleteCategoryFromBook(savedBook.getId(), defaultCategory.getId());

        Assertions.assertThat(updatedBook.getCategories()).isEmpty();
    }

    @Test
    @DisplayName("Добавление автора к книге")
    void testAddAuthorToBook() {
        Book book = createValidBook();
        Book savedBook = bookService.save(book);

        Book updatedBook = bookService.addAuthorToBook(savedBook.getId(), 2L);

        Assertions.assertThat(updatedBook.getAuthors()).hasSize(2);
        Assertions.assertThat(updatedBook.getAuthors()).contains(2L);
    }

    @Test
    @DisplayName("Удаление автора из книги")
     void testDeleteAuthorFromBook() {
        Book book = createValidBook();
        Book savedBook = bookService.save(book);

        Book updatedBook = bookService.deleteAuthorFromBook(savedBook.getId(), 1L);

        Assertions.assertThat(updatedBook.getAuthors()).isEmpty();
    }


    @Test
    @Deployment(resources = "book-approval.bpmn")  // Указываем путь к вашему BPMN файлу
    public void testStartBookApprovalProcess() {
        // Уникальный ID книги для теста
        Book book = createValidBook();
        Book savedBook = bookService.save(book);

    }
}