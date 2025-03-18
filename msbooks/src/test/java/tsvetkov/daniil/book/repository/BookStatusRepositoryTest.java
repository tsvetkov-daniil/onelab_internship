package tsvetkov.daniil.book.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.dto.BookStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataJpaTest
@Transactional
@Rollback
class BookStatusRepositoryTest {
    private BookStatusRepository bookStatusRepository;

    @Autowired
    BookStatusRepositoryTest(BookStatusRepository bookStatusRepository) {
        this.bookStatusRepository = bookStatusRepository;
    }

    @Test
    @DisplayName("Поиск статуса по названию")
    void shouldFindStatusByTitle()
    {
        final String title = "added";
        final BookStatus status = createStatus(title);

        assertThatCode(()-> bookStatusRepository.save(status)).doesNotThrowAnyException();

        final Optional<BookStatus> foundedStatus = bookStatusRepository.findByTitle(title);

        assertThat(foundedStatus).isPresent()
                .hasValueSatisfying(s -> assertThat(s.getTitle()).isEqualTo(title));
    }

    private BookStatus createStatus(String name) {
        return BookStatus.builder()
                .title(name)
                .description("Книга опубликована")
                .build();
    }
}
