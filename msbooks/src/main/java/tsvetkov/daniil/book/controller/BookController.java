package tsvetkov.daniil.book.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsvetkov.daniil.book.dto.*;
import tsvetkov.daniil.book.service.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class BookController {

    private final BookService bookService;
    private final BookStatusService bookStatusService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    private final LanguageService languageService;
    private final ScoreService scoreService;

    public BookController(BookService bookService, BookStatusService bookStatusService,
                          CategoryService categoryService, CommentService commentService,
                          LanguageService languageService, ScoreService scoreService) {
        this.bookService = bookService;
        this.bookStatusService = bookStatusService;
        this.categoryService = categoryService;
        this.commentService = commentService;
        this.languageService = languageService;
        this.scoreService = scoreService;
    }

    // Эндпоинты для Book
    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book savedBook = bookService.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.findById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/books")
    public ResponseEntity<Set<Book>> getAllBooks(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        Set<Book> books = bookService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok(books);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        book.setId(id);
        Book updatedBook = bookService.save(book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/books/{id}/categories/{categoryId}")
    public ResponseEntity<Book> addCategoryToBook(@PathVariable Long id, @PathVariable Long categoryId) {
        Book updatedBook = bookService.addCategoryToBook(id, categoryId);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/books/{id}/categories/{categoryId}")
    public ResponseEntity<Book> removeCategoryFromBook(@PathVariable Long id, @PathVariable Long categoryId) {
        Book updatedBook = bookService.deleteCategoryFromBook(id, categoryId);
        return ResponseEntity.ok(updatedBook);
    }

    @PostMapping("/books/{id}/authors/{authorId}")
    public ResponseEntity<Book> addAuthorToBook(@PathVariable Long id, @PathVariable Long authorId) {
        Book updatedBook = bookService.addAuthorToBook(id, authorId);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/books/{id}/authors/{authorId}")
    public ResponseEntity<Book> removeAuthorFromBook(@PathVariable Long id, @PathVariable Long authorId) {
        Book updatedBook = bookService.deleteAuthorFromBook(id, authorId);
        return ResponseEntity.ok(updatedBook);
    }

    @PatchMapping("/books/{id}/status/{statusId}")
    public ResponseEntity<Book> changeBookStatus(@PathVariable Long id, @PathVariable Long statusId) {
        Book updatedBook = bookService.changeStatus(id, statusId);
        return ResponseEntity.ok(updatedBook);
    }

    // Эндпоинты для BookStatus
    @PostMapping("/book-statuses")
    public ResponseEntity<BookStatus> createBookStatus(@Valid @RequestBody BookStatus bookStatus) {
        BookStatus savedStatus = bookStatusService.save(bookStatus);
        return new ResponseEntity<>(savedStatus, HttpStatus.CREATED);
    }

    @GetMapping("/book-statuses/{id}")
    public ResponseEntity<BookStatus> getBookStatusById(@PathVariable Long id) {
        BookStatus bookStatus = bookStatusService.findById(id);
        return ResponseEntity.ok(bookStatus);
    }

    @GetMapping("/book-statuses")
    public ResponseEntity<Set<BookStatus>> getAllBookStatuses(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                              @RequestParam(defaultValue = "10") Integer pageSize) {
        Set<BookStatus> statuses = bookStatusService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok(statuses);
    }

    @DeleteMapping("/book-statuses/{id}")
    public ResponseEntity<Void> deleteBookStatus(@PathVariable Long id) {
        bookStatusService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/book-statuses/title/{title}")
    public ResponseEntity<BookStatus> getBookStatusByTitle(@PathVariable String title) {
        Optional<BookStatus> bookStatus = bookStatusService.findByTitle(title);
        return bookStatus.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Эндпоинты для Category
    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        Category savedCategory = categoryService.save(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories/{id}/subcategories")
    public ResponseEntity<Set<Category>> getSubcategories(@PathVariable Long id) {
        Set<Category> subcategories = categoryService.findSubcategories(id);
        return ResponseEntity.ok(subcategories);
    }

    // Эндпоинты для Comment
    @PostMapping("/comments")
    public ResponseEntity<Comment> createComment(@Valid @RequestBody Comment comment) {
        Comment savedComment = commentService.save(comment);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.findById(id);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/books/{bookId}/comments")
    public ResponseEntity<Set<Comment>> getCommentsByBookId(@PathVariable Long bookId,
                                                            @RequestParam(defaultValue = "0") Integer pageNumber,
                                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        Set<Comment> comments = commentService.findAllByBookId(bookId, pageNumber, pageSize);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Эндпоинты для Language
    @PostMapping("/languages")
    public ResponseEntity<Language> createLanguage(@Valid @RequestBody Language language) {
        Language savedLanguage = languageService.save(language);
        return new ResponseEntity<>(savedLanguage, HttpStatus.CREATED);
    }

    @GetMapping("/languages/{id}")
    public ResponseEntity<Language> getLanguageById(@PathVariable Long id) {
        Language language = languageService.findById(id);
        return ResponseEntity.ok(language);
    }

    @GetMapping("/languages")
    public ResponseEntity<List<Language>> getAllLanguages() {
        List<Language> languages = languageService.findAll();
        return ResponseEntity.ok(languages);
    }

    @DeleteMapping("/languages/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        languageService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/languages/name/{name}")
    public ResponseEntity<Language> getLanguageByName(@PathVariable String name) {
        Optional<Language> language = languageService.findByName(name);
        return language.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Эндпоинты для Score
    @PostMapping("/scores")
    public ResponseEntity<Score> createScore(@Valid @RequestBody Score score) {
        Score savedScore = scoreService.save(score);
        return new ResponseEntity<>(savedScore, HttpStatus.CREATED);
    }

    @GetMapping("/scores/{id}")
    public ResponseEntity<Score> getScoreById(@PathVariable Long id) {
        Score score = scoreService.findById(id);
        return ResponseEntity.ok(score);
    }

    @GetMapping("/scores/value/{value}")
    public ResponseEntity<Score> getScoreByValue(@PathVariable Byte value) {
        Optional<Score> score = scoreService.findByValue(value);
        return score.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/scores")
    public ResponseEntity<Set<Score>> getAllScores() {
        Set<Score> scores = scoreService.findAll();
        return ResponseEntity.ok(scores);
    }

    @DeleteMapping("/scores/{id}")
    public ResponseEntity<Void> deleteScore(@PathVariable Long id) {
        scoreService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
