package tsvetkov.daniil.book.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.entity.*;
import tsvetkov.daniil.book.repository.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class BookControllerTest {

    private final TestRestTemplate restTemplate;
    private final BookRepository bookRepository;
    private final BookStatusRepository bookStatusRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final LanguageRepository languageRepository;
    private final ScoreRepository scoreRepository;

    @Autowired
    BookControllerTest(TestRestTemplate restTemplate, BookRepository bookRepository,
                       BookStatusRepository bookStatusRepository, CategoryRepository categoryRepository,
                       CommentRepository commentRepository, LanguageRepository languageRepository,
                       ScoreRepository scoreRepository) {
        this.restTemplate = restTemplate;
        this.bookRepository = bookRepository;
        this.bookStatusRepository = bookStatusRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.languageRepository = languageRepository;
        this.scoreRepository = scoreRepository;
    }

    private Book createValidBook() {
        Language language = languageRepository.save(Language.builder().name("Русский").build());
        BookStatus status = bookStatusRepository.save(BookStatus.builder().title("Available").build());
        return Book.builder()
                .title("Тестовая книга")
                .description("Описание")
                .publishDate(new Date())
                .coverUrl("http://example.com/cover.jpg")
                .price(100.0f)
                .language(language)
                .status(status) // Передаём полный объект BookStatus
                .authors(new HashSet<>(Set.of(1L)))
                .categories(new HashSet<>())
                .build();
    }

    private Category createValidCategory() {
        return Category.builder()
                .name("Фантастика")
                .description("Жанр фантастики")
                .build();
    }

    private Comment createValidComment(Book book) {
        Score score = scoreRepository.save(Score.builder().value((byte) 5).build());
        return Comment.builder()
                .text("Тестовый комментарий")
                .book(book)
                .readerId(1L)
                .score(score)
                .build();
    }

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        bookRepository.deleteAll();
        categoryRepository.deleteAll();
        bookStatusRepository.deleteAll();
        languageRepository.deleteAll();
        scoreRepository.deleteAll();
    }

    @Test
    @DisplayName("Успешное создание новой книги")
    void testCreateBook() {
        Book book = createValidBook();
        ResponseEntity<Book> response = restTemplate.postForEntity("/api/v1/books", book, Book.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getTitle()).isEqualTo("Тестовая книга");
    }

    @Test
    @DisplayName("Получение книги по ID")
    void testGetBookById() {
        Book book = createValidBook();
        ResponseEntity<Book> createResponse = restTemplate.postForEntity("/api/v1/books", book, Book.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Book> response = restTemplate.getForEntity("/api/v1/books/{id}", Book.class, id);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getId()).isEqualTo(id);
        Assertions.assertThat(response.getBody().getTitle()).isEqualTo("Тестовая книга");
    }

    @Test
    @DisplayName("Получение всех книг с пагинацией")
    void testGetAllBooks() {
        restTemplate.postForEntity("/api/v1/books", createValidBook(), Book.class);
        Book anotherBook = createValidBook();
        anotherBook.setTitle("Другая книга");
        restTemplate.postForEntity("/api/v1/books", anotherBook, Book.class);

        ResponseEntity<Set> response = restTemplate.getForEntity("/api/v1/books?pageNumber=0&pageSize=1", Set.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).hasSize(1);
    }

    @Test
    @DisplayName("Обновление книги")
    void testUpdateBook() {
        Book book = createValidBook();
        ResponseEntity<Book> createResponse = restTemplate.postForEntity("/api/v1/books", book, Book.class);
        Long id = createResponse.getBody().getId();

        Book updatedBook = createValidBook();
        updatedBook.setTitle("Обновлённая книга");
        HttpEntity<Book> request = new HttpEntity<>(updatedBook);
        ResponseEntity<Book> response = restTemplate.exchange("/api/v1/books/{id}", HttpMethod.PUT, request, Book.class, id);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getTitle()).isEqualTo("Обновлённая книга");
        Assertions.assertThat(response.getBody().getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Удаление книги")
    void testDeleteBook() {
        Book book = createValidBook();
        ResponseEntity<Book> createResponse = restTemplate.postForEntity("/api/v1/books", book, Book.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Void> response = restTemplate.exchange("/api/v1/books/{id}", HttpMethod.DELETE, null, Void.class, id);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Добавление категории к книге")
    void testAddCategoryToBook() {
        Book book = createValidBook();
        ResponseEntity<Book> bookResponse = restTemplate.postForEntity("/api/v1/books", book, Book.class);
        Long bookId = bookResponse.getBody().getId();

        Category category = createValidCategory();
        ResponseEntity<Category> categoryResponse = restTemplate.postForEntity("/api/v1/categories", category, Category.class);
        Long categoryId = categoryResponse.getBody().getId();

        ResponseEntity<Book> response = restTemplate.postForEntity("/api/v1/books/{id}/categories/{categoryId}", null, Book.class, bookId, categoryId);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getCategories()).hasSize(1);
    }

    @Test
    @DisplayName("Успешное создание новой категории")
    void testCreateCategory() {
        Category category = createValidCategory();
        ResponseEntity<Category> response = restTemplate.postForEntity("/api/v1/categories", category, Category.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Фантастика");
    }

    @Test
    @DisplayName("Успешное создание нового комментария")
    void testCreateComment() {
        Book book = createValidBook();
        ResponseEntity<Book> bookResponse = restTemplate.postForEntity("/api/v1/books", book, Book.class);
        Comment comment = createValidComment(bookResponse.getBody());

        ResponseEntity<Comment> response = restTemplate.postForEntity("/api/v1/comments", comment, Comment.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getText()).isEqualTo("Тестовый комментарий");
    }

    @Test
    @DisplayName("Получение комментариев по книге")
    void testGetCommentsByBookId() {
        Book book = createValidBook();
        ResponseEntity<Book> bookResponse = restTemplate.postForEntity("/api/v1/books", book, Book.class);
        Long bookId = bookResponse.getBody().getId();

        Comment comment = createValidComment(bookResponse.getBody());
        restTemplate.postForEntity("/api/v1/comments", comment, Comment.class);

        ResponseEntity<Set> response = restTemplate.getForEntity("/api/v1/books/{bookId}/comments?pageNumber=0&pageSize=10", Set.class, bookId);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).hasSize(1);
    }

    @Test
    @DisplayName("Успешное создание нового языка")
    void testCreateLanguage() {
        Language language = Language.builder().name("Английский").build();
        ResponseEntity<Language> response = restTemplate.postForEntity("/api/v1/languages", language, Language.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Английский");
    }

    @Test
    @DisplayName("Успешное создание новой оценки")
    void testCreateScore() {
        Score score = Score.builder().value((byte) 4).build();
        ResponseEntity<Score> response = restTemplate.postForEntity("/api/v1/scores", score, Score.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getValue()).isEqualTo((byte) 4);
    }

    @Test
    @DisplayName("Успешное создание нового статуса книги")
    void testCreateBookStatus() {
        BookStatus status = BookStatus.builder().title("Out of Stock").build();
        ResponseEntity<BookStatus> response = restTemplate.postForEntity("/api/v1/book-statuses", status, BookStatus.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getTitle()).isEqualTo("Out of Stock");
    }
}