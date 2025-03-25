package tsvetkov.daniil.search.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tsvetkov.daniil.dto.BookDTO;
import tsvetkov.daniil.dto.CategoryDTO;
import tsvetkov.daniil.dto.ReaderDTO;
import tsvetkov.daniil.dto.ReviewDTO;
import tsvetkov.daniil.search.entity.Author;
import tsvetkov.daniil.search.entity.Book;
import tsvetkov.daniil.search.entity.Category;
import tsvetkov.daniil.search.entity.Review;
import tsvetkov.daniil.search.service.AuthorService;
import tsvetkov.daniil.search.service.BookSearchService;
import tsvetkov.daniil.search.service.CategoryService;
import tsvetkov.daniil.search.service.ReviewService;
import tsvetkov.daniil.util.mapper.Mapper;

@RequiredArgsConstructor
@Service
public class EventConsumer {
    private final AuthorService authorService;
    private final BookSearchService bookService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;
    private final Mapper mapper;

    @KafkaListener(topics = "author-create-search")
    public void consumeAuthorCreate(ReaderDTO authorDTO) {
        Author author = mapper.map(authorDTO, Author.class);
        authorService.save(author);
    }

    @KafkaListener(topics = "author-delete-search")
    public void consumeAuthorDelete(Long authorId) {
        authorService.deleteById(authorId);
    }

    @KafkaListener(topics = "book-create-search")
    public void consumeBookCreate(BookDTO bookDTO) {
        Book book = mapper.map(bookDTO, Book.class);
        bookService.save(book);
    }

    @KafkaListener(topics = "book-delete-search")
    public void consumeBookDelete(Long bookId) {
        bookService.deleteById(bookId);
    }

    @KafkaListener(topics = "category-create-search")
    public void consumeCategoryCreate(CategoryDTO categoryDTO) {
        Category category = mapper.map(categoryDTO, Category.class);
        categoryService.save(category);
    }

    @KafkaListener(topics = "category-delete-search")
    public void consumeCategoryDelete(Long categoryId) {
        categoryService.deleteById(categoryId);
    }

    @KafkaListener(topics = "review-create-search")
    public void consumeReviewCreate(ReviewDTO reviewDTO) {
        Review review = mapper.map(reviewDTO, Review.class);
        reviewService.save(review);
    }

    @KafkaListener(topics = "review-delete-search")
    public void consumeReviewDelete(Long reviewId) {
        reviewService.deleteById(reviewId);
    }
}

