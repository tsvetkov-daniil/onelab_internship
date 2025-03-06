package servicetest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tsvetkov.daniil.dto.Book;
import tsvetkov.daniil.dto.Reader;
import tsvetkov.daniil.dto.Review;
import tsvetkov.daniil.dto.Score;
import tsvetkov.daniil.repository.ReviewRepository;
import tsvetkov.daniil.service.ReviewService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    public void testCreateReview() {
        Review review = Review.builder()
                .id(1L)
                .text("Great book!")
                .score(Score.builder().value((byte) 5).build())
                .book(Book.builder().id(1L).build())
                .author(Reader.builder().id(1L).build())
                .build();

        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review savedReview = reviewService.create(review);
        assertNotNull(savedReview);
        assertEquals("Great book!", savedReview.getText());
    }

    @Test
    public void testFindById() {
        Review review = Review.builder()
                .id(1L)
                .text("Amazing!")
                .score(Score.builder().value((byte) 5).build())
                .book(Book.builder().id(1L).build())
                .author(Reader.builder().id(1L).build())
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        Optional<Review> foundReview = reviewService.findById(1L);
        assertTrue(foundReview.isPresent());
        assertEquals("Amazing!", foundReview.get().getText());
    }

    @Test
    public void testFindByBookId() {
        Set<Review> reviews = new HashSet<>();
        Review review = Review.builder()
                .id(1L)
                .text("Interesting book")
                .score(Score.builder().value((byte) 4).build())
                .book(Book.builder().id(1L).build())
                .author(Reader.builder().id(1L).build())
                .build();
        reviews.add(review);

        when(reviewRepository.findByBookId(1L)).thenReturn(reviews);

        Set<Review> foundReviews = reviewService.findByBookId(1L);
        assertNotNull(foundReviews);
        assertEquals(1, foundReviews.size());
    }

    @Test
    public void testFindByAuthorId() {
        Set<Review> reviews = new HashSet<>();
        Review review = Review.builder()
                .id(1L)
                .text("Fantastic!")
                .score(Score.builder().value((byte) 5).build())
                .book(Book.builder().id(1L).build())
                .author(Reader.builder().id(1L).build())
                .build();
        reviews.add(review);

        when(reviewRepository.findByAuthorId(1L)).thenReturn(reviews);

        Set<Review> foundReviews = reviewService.findByAuthorId(1L);
        assertNotNull(foundReviews);
        assertEquals(1, foundReviews.size());
    }

    @Test
    public void testUpdateReview() {
        Review existingReview = Review.builder()
                .id(1L)
                .text("Old review")
                .score(Score.builder().value((byte) 3).build())
                .book(Book.builder().id(1L).build())
                .author(Reader.builder().id(1L).build())
                .build();

        Review updatedReview = Review.builder()
                .id(1L)
                .text("Updated review")
                .score(Score.builder().value((byte) 5).build())
                .book(Book.builder().id(1L).build())
                .author(Reader.builder().id(1L).build())
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(updatedReview);

        Review result = reviewService.update(1L, updatedReview);
        assertNotNull(result);
        assertEquals("Updated review", result.getText());
        assertEquals(5, result.getScore().getValue().intValue());
    }

    @Test
    public void testDeleteReview() {
        Review review = Review.builder()
                .id(1L)
                .text("To be deleted")
                .score(Score.builder().value((byte) 2).build())
                .book(Book.builder().id(1L).build())
                .author(Reader.builder().id(1L).build())
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        reviewService.delete(1L);
        verify(reviewRepository, times(1)).deleteById(1L);
    }
}
