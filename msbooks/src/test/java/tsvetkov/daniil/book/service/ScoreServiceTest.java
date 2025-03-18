package tsvetkov.daniil.book.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.dto.Score;
import tsvetkov.daniil.book.repository.ScoreRepository;

import java.util.Optional;
import java.util.Set;

@SpringBootTest
@Transactional
class ScoreServiceTest {

    private final ScoreService scoreService;
    private final ScoreRepository scoreRepository;

    @Autowired
    ScoreServiceTest(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
        this.scoreService = new ScoreService(scoreRepository);
    }

    private Score createValidScore() {
        return Score.builder()
                .value((byte) 5)
                .build();
    }

    @BeforeEach
    void setUp() {
        scoreRepository.deleteAll();
    }

    @Test
    @DisplayName("Успешное сохранение новой оценки")
    void testSave() {
        Score score = createValidScore();
        Score savedScore = scoreService.save(score);

        Assertions.assertThat(savedScore.getId()).isNotNull();
        Assertions.assertThat(savedScore.getValue()).isEqualTo((byte) 5);
    }

    @Test
    @DisplayName("Поиск оценки по ID")
    void testFindById() {
        Score score = createValidScore();
        Score savedScore = scoreService.save(score);

        Score foundScore = scoreService.findById(savedScore.getId());

        Assertions.assertThat(foundScore).isEqualTo(savedScore);
    }

    @Test
    @DisplayName("Ошибка при поиске несуществующей оценки по ID")
    void testFindByIdNotFound() {
        Assertions.assertThatThrownBy(() -> scoreService.findById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Оценка с id: 999 не найдена");
    }

    @Test
    @DisplayName("Поиск оценки по значению")
    void testFindByValue() {
        Score score = createValidScore();
        scoreService.save(score);

        Optional<Score> foundScore = scoreService.findByValue((byte) 5);

        Assertions.assertThat(foundScore).isPresent();
        Assertions.assertThat(foundScore.get().getValue()).isEqualTo((byte) 5);
    }

    @Test
    @DisplayName("Поиск несуществующей оценки по значению")
    void testFindByValueNotFound() {
        Optional<Score> foundScore = scoreService.findByValue((byte) 3);

        Assertions.assertThat(foundScore).isNotPresent();
    }

    @Test
    @DisplayName("Получение всех оценок")
    void testFindAll() {
        scoreService.save(createValidScore());
        scoreService.save(Score.builder().value((byte) 4).build());

        Set<Score> scores = scoreService.findAll();

        Assertions.assertThat(scores).hasSize(2);
        Assertions.assertThat(scores).extracting("value").contains((byte) 5, (byte) 4);
    }

    @Test
    @DisplayName("Удаление оценки по ID")
    void testDeleteById() {
        Score score = createValidScore();
        Score savedScore = scoreService.save(score);

        scoreService.deleteById(savedScore.getId());

        Assertions.assertThatThrownBy(() -> scoreService.findById(savedScore.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Оценка с id: " + savedScore.getId() + " не найдена");
    }

    @Test
    @DisplayName("Удаление всех оценок")
    void testDeleteAll() {
        scoreService.save(createValidScore());
        scoreService.save(Score.builder().value((byte) 4).build());

        scoreService.deleteAll();

        Assertions.assertThat(scoreService.findAll()).isEmpty();
    }
}