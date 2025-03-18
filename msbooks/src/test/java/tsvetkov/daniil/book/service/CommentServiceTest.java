package tsvetkov.daniil.book.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.dto.Book;
import tsvetkov.daniil.book.dto.Category;
import tsvetkov.daniil.book.dto.Comment;
import tsvetkov.daniil.book.dto.Language;
import tsvetkov.daniil.book.dto.Score;
import tsvetkov.daniil.book.repository.CommentRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@Transactional
class CommentServiceTest {

    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final ScoreService scoreService;
    private final BookService bookService;
    private final LanguageService languageService;
    private final CategoryService categoryService;
    private final BookStatusService bookStatusService;

    @Autowired
    CommentServiceTest(CommentRepository commentRepository, ScoreService scoreService,
                       BookService bookService, LanguageService languageService,
                       CategoryService categoryService, BookStatusService bookStatusService) {
        this.commentRepository = commentRepository;
        this.scoreService = scoreService;
        this.bookService = bookService;
        this.languageService = languageService;
        this.categoryService = categoryService;
        this.bookStatusService = bookStatusService;
        this.commentService = new CommentService(commentRepository);
    }

    private Comment createValidComment() {
        // Создаём и сохраняем язык
        Language language = languageService.save(Language.builder().name("Русский").build());

        // Создаём и сохраняем категорию
        Category category = categoryService.save(Category.builder()
                .name("Фантастика")
                .description("Жанр фантастики")
                .build());

        // Создаём и сохраняем книгу
        Book book = Book.builder()
                .title("Тестовая книга")
                .description("Описание книги")
                .publishDate(new Date())
                .coverUrl("http://example.com/cover.jpg")
                .price(100.0f)
                .language(language)
                .status(bookStatusService.getDefaultStatus()) // Используем отдельный BookStatusService
                .authors(new HashSet<>(Set.of(1L)))
                .categories(new HashSet<>(Set.of(category)))
                .build();
        Book savedBook = bookService.save(book);

        // Создаём и сохраняем оценку
        Score score = scoreService.save(Score.builder().value((byte) 5).build());

        return Comment.builder()
                .text("Тестовый комментарий")
                .book(savedBook)
                .readerId(1L)
                .score(score)
                .build();
    }

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
    }

    @Test
    @DisplayName("Успешное сохранение нового комментария")
    void testSave() {
        Comment comment = createValidComment();
        Comment savedComment = commentService.save(comment);

        Assertions.assertThat(savedComment.getId()).isNotNull();
        Assertions.assertThat(savedComment.getText()).isEqualTo("Тестовый комментарий");
        Assertions.assertThat(savedComment.getBook().getId()).isNotNull();
        Assertions.assertThat(savedComment.getReaderId()).isEqualTo(1L);
        Assertions.assertThat(savedComment.getScore().getValue()).isEqualTo((byte) 5);
    }

    @Test
    @DisplayName("Поиск комментария по ID")
    void testFindById() {
        Comment comment = createValidComment();
        Comment savedComment = commentService.save(comment);

        Comment foundComment = commentService.findById(savedComment.getId());

        Assertions.assertThat(foundComment).isEqualTo(savedComment);
    }

    @Test
    @DisplayName("Ошибка при поиске несуществующего комментария по ID")
    void testFindByIdNotFound() {
        Assertions.assertThatThrownBy(() -> commentService.findById(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Поиск всех комментариев по ID книги с пагинацией")
    void testFindAllByBookId() {
        Comment comment1 = createValidComment();
        Comment savedComment1 = commentService.save(comment1);

        Comment comment2 = Comment.builder()
                .text("Другой комментарий")
                .book(savedComment1.getBook())
                .readerId(2L)
                .score(scoreService.save(Score.builder().value((byte) 4).build()))
                .build();
        commentService.save(comment2);

        Set<Comment> comments = commentService.findAllByBookId(savedComment1.getBook().getId(), 0, 1);

        Assertions.assertThat(comments).hasSize(1);
        Assertions.assertThat(comments).extracting("text").containsAnyOf("Тестовый комментарий", "Другой комментарий");
    }

    @Test
    @DisplayName("Удаление комментария по ID")
    void testDelete() {
        Comment comment = createValidComment();
        Comment savedComment = commentService.save(comment);

        commentService.delete(savedComment.getId());

        Assertions.assertThatThrownBy(() -> commentService.findById(savedComment.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Удаление всех комментариев")
    void testDeleteAll() {
        Comment comment1 = createValidComment();
        Comment savedComment1 = commentService.save(comment1);

        Comment comment2 = Comment.builder()
                .text("Другой комментарий")
                .book(savedComment1.getBook())
                .readerId(2L)
                .score(scoreService.save(Score.builder().value((byte) 4).build()))
                .build();
        commentService.save(comment2);

        commentService.deleteAll();

        Assertions.assertThat(commentRepository.findAll(PageRequest.of(0, 10)).getContent()).isEmpty();
    }
}