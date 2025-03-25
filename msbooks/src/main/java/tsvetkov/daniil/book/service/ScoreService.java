package tsvetkov.daniil.book.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tsvetkov.daniil.book.entity.Score;
import tsvetkov.daniil.book.exception.ScoreNotFoundException;
import tsvetkov.daniil.book.repository.ScoreRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
@AllArgsConstructor
@Validated
public class ScoreService {

    private final ScoreRepository scoreRepository;

    @Transactional
    public Score save(@Valid Score score) {
        return scoreRepository.save(score);
    }

    @Transactional(readOnly = true)
    public Score findById(Long id) {
        return scoreRepository.findById(id)
                .orElseThrow(ScoreNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Optional<Score> findByValue(Byte value) {
        return scoreRepository.findByValue(value);
    }

    @Transactional(readOnly = true)
    public Set<Score> findAll(Integer pageNumber, Integer pageSize) {
        return new HashSet<>(scoreRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent());
    }

    @Transactional
    public void deleteById(Long id) {
        findById(id);
        scoreRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        scoreRepository.deleteAll();
    }
}
