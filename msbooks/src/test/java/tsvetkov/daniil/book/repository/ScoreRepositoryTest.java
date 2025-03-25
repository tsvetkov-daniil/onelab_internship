package tsvetkov.daniil.book.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.entity.Score;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataJpaTest
@Transactional
@Rollback
class ScoreRepositoryTest {

    private final  ScoreRepository scoreRepository;

    @Autowired
    ScoreRepositoryTest(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @BeforeEach
    void setUp() {
        scoreRepository.deleteAll();
    }

    @Test
    @DisplayName("Поиск рейтинга по значению")
    void testFindByValue() {
        byte score = 5;
        assertThatCode(()->scoreRepository.save(Score.builder().value(score).build())).doesNotThrowAnyException();

        Optional<Score> foundScore = scoreRepository.findByValue(score);

        assertThat(foundScore).isPresent()
                .hasValueSatisfying(s -> assertThat(s.getValue()).isEqualTo(score));
    }

    @Test
    @DisplayName("Поиск несуществующего рейтинга")
    void testFindByValueNotFound() {
        Optional<Score> foundScore = scoreRepository.findByValue((byte) 10);

        assertThat(foundScore).isNotPresent();
    }
}
