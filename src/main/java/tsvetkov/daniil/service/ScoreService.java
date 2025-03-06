package tsvetkov.daniil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.dto.Score;
import tsvetkov.daniil.repository.ScoreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ScoreService {
    private final ScoreRepository scoreRepository;

    @Autowired
    public ScoreService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @Transactional
    public Score create(Score score) {
        return scoreRepository.save(score);
    }

    public Optional<Score> findById(Long id) {
        return scoreRepository.findById(id);
    }

    public Optional<Score> findByValue(Byte value) {
        return scoreRepository.findByValue(value);
    }

    public List<Score> findAll() {
        return scoreRepository.findAll();
    }

    @Transactional
    public Score update(Long id, Score updatedScore) {
        Score existingScore = scoreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Оценка не найдена"));

        existingScore.setValue(updatedScore.getValue());

        return scoreRepository.save(existingScore);
    }

    @Transactional
    public void delete(Long id) {
        scoreRepository.deleteById(id);
    }
}
