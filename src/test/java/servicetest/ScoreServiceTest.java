package servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tsvetkov.daniil.dto.Score;
import tsvetkov.daniil.repository.ScoreRepository;
import tsvetkov.daniil.service.ScoreService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ScoreServiceTest {
    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    private ScoreService scoreService;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        scoreService = new ScoreService(scoreRepository);
    }

    @Test
    public void testCreateScore() {
        Score score = Score.builder()
                .value((byte) 5)
                .build();

        Mockito.when(scoreRepository.save(score)).thenReturn(score);

        Score createdScore = scoreService.create(score);

        assertNotNull(createdScore);
        assertEquals((byte) 5, createdScore.getValue());
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Score score = Score.builder()
                .id(id)
                .value((byte) 5)
                .build();

        Mockito.when(scoreRepository.findById(id)).thenReturn(Optional.of(score));

        Optional<Score> foundScore = scoreService.findById(id);

        assertTrue(foundScore.isPresent());
        assertEquals(id, foundScore.get().getId());
    }

    @Test
    public void testFindByValue() {
        byte value = 5;
        Score score = Score.builder()
                .value(value)
                .build();
        Mockito.when(scoreRepository.findByValue(value)).thenReturn(Optional.of(score));

        Optional<Score> foundScore = scoreService.findByValue(value);

        assertTrue(foundScore.isPresent());
        assertEquals(value, foundScore.get().getValue());
    }

    @Test
    public void testFindAll() {
        Score score1 = Score.builder()
                .value((byte) 5)
                .build();
        Score score2 = Score.builder().build();
        score2.setValue((byte) 3);

        List<Score> scores = Arrays.asList(score1, score2);

        Mockito.when(scoreRepository.findAll()).thenReturn(scores);

        List<Score> allScores = scoreService.findAll();

        assertNotNull(allScores);
        assertEquals(2, allScores.size());
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        Score existingScore = Score.builder()
                .id(id)
                .value((byte) 5)
                .build();

        Score updatedScore = Score.builder()
                .value((byte) 10)
                .build();
        Mockito.when(scoreRepository.findById(id)).thenReturn(Optional.of(existingScore));
        Mockito.when(scoreRepository.save(existingScore)).thenReturn(existingScore);

        Score updated = scoreService.update(id, updatedScore);

        assertNotNull(updated);
        assertEquals((byte) 10, updated.getValue());
    }

    @Test
    public void testDelete() {
        Long id = 1L;

        Mockito.doNothing().when(scoreRepository).deleteById(id);

        scoreService.delete(id);

        Mockito.verify(scoreRepository, Mockito.times(1)).deleteById(id);
    }
}
