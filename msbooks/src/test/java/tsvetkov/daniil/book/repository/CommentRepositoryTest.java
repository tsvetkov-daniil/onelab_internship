package tsvetkov.daniil.book.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.entity.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Rollback
class CommentRepositoryTest {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final BookStatusRepository bookStatusRepository;
    private final ScoreRepository scoreRepository;

    private Book book;
    private Score score;

    @Autowired
    CommentRepositoryTest(CommentRepository commentRepository, BookRepository bookRepository, CategoryRepository categoryRepository, LanguageRepository languageRepository, BookStatusRepository bookStatusRepository, ScoreRepository scoreRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.languageRepository = languageRepository;
        this.bookStatusRepository = bookStatusRepository;
        this.scoreRepository = scoreRepository;
    }

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        bookRepository.deleteAll();

        createReader();
        createScore();
    }

    // TODO переделать, добавить к книге этот метод
    @Test
    @DisplayName("Поиск коментариев по книге")
    void testFindByBookId() {
        Comment comment1 = commentRepository.save(Comment.builder().book(book)
                .readerId(0L).score(score).text("First comment").build());

        Comment comment2 = commentRepository.save(Comment.builder().book(book)
                .readerId(0L).score(score).text("Second comment").build());

        Page<Comment> commentsPage = commentRepository.findByBook_Id(book.getId(), PageRequest.of(0, 10));

        assertThat(commentsPage.getTotalElements()).isEqualTo(2);
        assertThat(commentsPage.getContent()).containsExactlyInAnyOrder(comment1, comment2);
    }

    private void createScore() {
        score = Score.builder()
                .value((byte) 1)
                .build();
        scoreRepository.save(score);
    }

    private void createReader() {
        Language language = languageRepository.save(Language.builder().name("English").build());

        BookStatus status = bookStatusRepository.save(BookStatus.builder().title("Available").description("Доступна").build());
        Category category = categoryRepository.save(Category.builder().name("Fiction").build());
        Set<Category> categories = new HashSet<>();
        categories.add(category);

        book = Book.builder()
                .title("Test Book")
                .language(language)
                .coverUrl("https://vk.com")
                .status(status)
                .categories(categories)
                .publishDate(new Date())
                .price(19.99f)
                .description("A great book")
                .authors(Set.of(1L))
                .build();

        bookRepository.save(book);
    }
}
