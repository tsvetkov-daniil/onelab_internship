package tsvetkov.daniil.review.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tsvetkov.daniil.review.dto.Review;
import tsvetkov.daniil.review.exception.ReviewNotFoundException;
import tsvetkov.daniil.review.repository.ReviewRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Validated
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public Review save(@Valid Review review) {
        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public Review findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException());
    }

    @Transactional(readOnly = true)
    public Set<Review> findByBookId(Long bookId, Integer pageNumber, Integer pageSize) {
        return new HashSet<>(reviewRepository.findByBookId(bookId, PageRequest.of(pageNumber, pageSize)).getContent());
    }

    @Transactional(readOnly = true)
    public Set<Review> findByAuthorId(Long authorId, Integer pageNumber, Integer pageSize) {
        return new HashSet<>(reviewRepository.findByAuthorId(authorId, PageRequest.of(pageNumber, pageSize)).getContent());
    }

    @Transactional
    public void deleteById(Long id) {
        findById(id);
        reviewRepository.deleteById(id);
    }
}
