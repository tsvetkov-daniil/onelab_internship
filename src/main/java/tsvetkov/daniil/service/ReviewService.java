package tsvetkov.daniil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.dto.Review;
import tsvetkov.daniil.repository.ReviewRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public Review create(Review review) {
        return reviewRepository.save(review);
    }

    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public Set<Review> findByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    public Set<Review> findByAuthorId(Long authorId) {
        return reviewRepository.findByAuthorId(authorId);
    }

    @Transactional
    public Review update(Long id, Review updatedReview) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отзыв не найден"));

        existingReview.setText(updatedReview.getText());
        existingReview.setScore(updatedReview.getScore());
        existingReview.setBook(updatedReview.getBook());
        existingReview.setAuthor(updatedReview.getAuthor());

        return reviewRepository.save(existingReview);
    }

    @Transactional
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
}
