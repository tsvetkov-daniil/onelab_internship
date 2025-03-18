package tsvetkov.daniil.search.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tsvetkov.daniil.search.dto.Book;
import tsvetkov.daniil.search.service.AuthorSearchService;
import tsvetkov.daniil.search.service.BookSearchService;
import tsvetkov.daniil.search.service.CategoryService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private final BookSearchService bookSearchService;
    private final AuthorSearchService authorSearchService;
    private final CategoryService categoryService;

    public SearchController(BookSearchService bookSearchService,
                            AuthorSearchService authorSearchService,
                            CategoryService categoryService) {
        this.bookSearchService = bookSearchService;
        this.authorSearchService = authorSearchService;
        this.categoryService = categoryService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        List<Book> books = null;
        try {
            books = bookSearchService.searchAcrossAllFields(query, pageNumber, pageSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Predicate<Book> hasRecentDate = book -> Optional.ofNullable(book.getPublishDate())
                .map(date -> LocalDateTime.now().minusYears(5).isBefore(date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()))
                .orElse(false);

        List<Book> filteredBooks = books.stream()
                .filter(hasRecentDate)
                .sorted(Comparator.comparing(Book::getPrice))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredBooks);
    }


    @GetMapping("/books/filter-by-price")
    public ResponseEntity<List<Book>> filterBooksByPrice(
            @RequestParam String query,
            @RequestParam float minPrice,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) throws IOException {

        List<Book> books = bookSearchService.searchAcrossAllFields(query, pageNumber, pageSize);


        List<Book> filteredBooks = books.stream()
                .filter(book -> Optional.ofNullable(book.getPrice())
                        .map(price -> price >= minPrice)
                        .orElse(false))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredBooks);
    }

    @GetMapping("/books/filter-by-date")
    public ResponseEntity<List<Book>> filterBooksByDate(
            @RequestParam String query,
            @RequestParam String dateAfter,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        LocalDateTime afterDate = LocalDateTime.parse(dateAfter);
        List<Book> books = null;
        try {
            books = bookSearchService.searchAcrossAllFields(query, pageNumber, pageSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Book> filteredBooks = books.stream()
                .filter(book -> Optional.ofNullable(book.getPublishDate())
                        .map(date -> date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime().isAfter(afterDate))
                        .orElse(false))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredBooks);
    }

    @GetMapping("/authors/suggest")
    public ResponseEntity<List<String>> suggestAuthors(
            @RequestParam String prefix,
            @RequestParam(defaultValue = "10") Integer size) throws IOException {
        List<String> suggestions = authorSearchService.suggestAuthorNames(prefix, size);
        long sequentialTime = System.nanoTime();
        List<String> sequential = suggestions.stream()
                .filter(s -> s.length() > 3)
                .collect(Collectors.toList());
        sequentialTime = System.nanoTime() - sequentialTime;

        long parallelTime = System.nanoTime();
        List<String> parallel = suggestions.parallelStream()
                .filter(s -> s.length() > 3)
                .collect(Collectors.toList());
        parallelTime = System.nanoTime() - parallelTime;

        System.out.println("Последовательный: " + sequentialTime + " ns, Паралельный: " + parallelTime + " ns");
        return ResponseEntity.ok(parallel);
    }

    @GetMapping("/categories/suggest")
    public ResponseEntity<List<String>> suggestCategories(
            @RequestParam String prefix,
            @RequestParam(defaultValue = "10") Integer size) throws IOException {
        List<String> suggestions = categoryService.suggestCategories(prefix, size);
        String combinedSuggestions = suggestions.stream()
                .reduce("", (acc, s) -> acc + (acc.isEmpty() ? "" : ", ") + s);

        Map<Integer, List<String>> groupedByLength = suggestions.stream()
                .collect(Collectors.groupingBy(String::length));

        return ResponseEntity.ok(List.of(combinedSuggestions.split(", ")));
    }
}