package tsvetkov.daniil.search.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsvetkov.daniil.search.dto.BookSearchRequest;
import tsvetkov.daniil.search.entity.Author;
import tsvetkov.daniil.search.entity.Book;
import tsvetkov.daniil.search.entity.Category;
import tsvetkov.daniil.search.entity.Review;
import tsvetkov.daniil.search.service.AuthorService;
import tsvetkov.daniil.search.service.BookSearchService;
import tsvetkov.daniil.search.service.CategoryService;
import tsvetkov.daniil.search.service.ReviewService;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/search")
@AllArgsConstructor
public class SearchController {

    private final BookSearchService bookSearchService;
    private final ReviewService reviewService;
    private final CategoryService categoryService;
    private final AuthorService authorService;

    @PostMapping("/books")
    public ResponseEntity<Set<Book>> searchBooks(
            @RequestBody BookSearchRequest request) throws IOException {
        Set<Book> books = bookSearchService.searchBooks(request);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/reviews/fuzzy")
    public ResponseEntity<Set<Review>> searchFuzzyReviews(
            @RequestParam  String searchText) {
        Set<Review> reviews = reviewService.findByFuzzyText(searchText);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/suggest/categories")
    public ResponseEntity<Set<Category>> suggestCategories(
            @RequestParam  String prefix,
            @RequestParam(defaultValue = "5") Integer size) throws IOException {
        Set<Category> suggestions = categoryService.suggestCategoriesByName(prefix, size);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/suggest/authors/name")
    public ResponseEntity<Set<Author>> suggestAuthorNames(
            @RequestParam  String prefix,
            @RequestParam(defaultValue = "5")  Integer size) throws IOException {
        Set<Author> suggestions = authorService.suggestAuthorName(prefix, size);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/suggest/authors/nickname")
    public ResponseEntity<Set<Author>> suggestAuthorNicknames(
            @RequestParam  String prefix,
            @RequestParam(defaultValue = "5")  Integer size) throws IOException {
        Set<Author> suggestions = authorService.suggestAuthorNickname(prefix, size);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/suggest/books")
    public ResponseEntity<Set<Book>> suggestBookTitles(
            @RequestParam String prefix,
            @RequestParam(defaultValue = "0")  Integer pageNumber,
            @RequestParam(defaultValue = "10")  Integer pageSize) throws IOException {
        Set<Book> suggestions = bookSearchService.searchAcrossAllFields(prefix, pageNumber,pageSize);
        return ResponseEntity.ok(suggestions);
    }

    @PostMapping("/books")
    public ResponseEntity<Set<Book>> searchBooksByFilter(
            @RequestBody BookSearchRequest request) throws IOException {
        Set<Book> books = bookSearchService.searchBooks(request);
        return ResponseEntity.ok(books);
    }
}