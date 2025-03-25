package tsvetkov.daniil.review.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsvetkov.daniil.review.dto.Review;
import tsvetkov.daniil.review.service.ReviewService;

import java.util.Set;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Review> saveReview(@RequestBody @Valid Review review) {
        return ResponseEntity.ok(reviewService.save(review));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.findById(id));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Set<Review>> getReviewsByBook(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseEntity.ok(reviewService.findByBookId(bookId, page, pageSize));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<Set<Review>> getReviewsByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseEntity.ok(reviewService.findByAuthorId(authorId, page, pageSize));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
