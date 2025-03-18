package tsvetkov.daniil.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.review.dto.Score;

@Repository
public interface Scorerepository extends JpaRepository<Score, Long> {
}
