package tsvetkov.daniil.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.review.dto.Review;

import java.util.Set;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Set<Review> findByBookId(Long bookId);
    Set<Review> findByAuthorId(Long authorId);
}
