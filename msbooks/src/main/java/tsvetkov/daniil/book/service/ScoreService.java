package tsvetkov.daniil.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.dto.Score;
import tsvetkov.daniil.book.repository.ScoreRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ScoreService {
    private final ScoreRepository scoreRepository;

    @Autowired
    public ScoreService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @Transactional
    public Score save(Score score) {
        return scoreRepository.save(score);
    }

    public Score findById(Long id) {
        return scoreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Оценка с id: "+ id+" не найдена"));
    }

    public Optional<Score> findByValue(Byte value) {
        return scoreRepository.findByValue(value);
    }

    public Set<Score> findAll() {
        return new HashSet<>(scoreRepository.findAll());
    }


    @Transactional
    public void deleteById(Long id) {
        scoreRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        scoreRepository.deleteAll();
    }
}
