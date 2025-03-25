package tsvetkov.daniil.review.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsvetkov.daniil.review.dto.Score;
import tsvetkov.daniil.review.service.ScoreService;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class ScoreController {
    private final ScoreService scoreService;

    @PostMapping("/scores")
    public ResponseEntity<Score> createScore(@Valid @RequestBody Score score) {
        Score savedScore = scoreService.save(score);
        return new ResponseEntity<>(savedScore, HttpStatus.CREATED);
    }

    @GetMapping("/scores/{id}")
    public ResponseEntity<Score> getScoreById(@PathVariable Long id) {
        Score score = scoreService.findById(id);
        return ResponseEntity.ok(score);
    }

    @GetMapping("/scores/value/{value}")
    public ResponseEntity<Score> getScoreByValue(@PathVariable Byte value) {
        Optional<Score> score = scoreService.findByValue(value);
        return score.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/scores")
    public ResponseEntity<Set<Score>> getAllScores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Set<Score> scores = scoreService.findAll(page, size);
        return ResponseEntity.ok(scores);
    }

    @DeleteMapping("/scores/{id}")
    public ResponseEntity<Void> deleteScore(@PathVariable Long id) {
        scoreService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
