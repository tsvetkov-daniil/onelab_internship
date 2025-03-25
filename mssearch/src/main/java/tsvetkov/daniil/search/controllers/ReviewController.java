package tsvetkov.daniil.search.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsvetkov.daniil.search.entity.Review;
import tsvetkov.daniil.search.service.ReviewService;

import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        reviewService.save(review);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/search/book/{bookId}")
    public ResponseEntity<Set<Review>> findByBookId(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.findByBookId(bookId));
    }

    @GetMapping("/search/text")
    public ResponseEntity<Set<Review>> findByText(@RequestParam String keyword) {
        return ResponseEntity.ok(reviewService.findByFuzzyText(keyword));
    }

}
