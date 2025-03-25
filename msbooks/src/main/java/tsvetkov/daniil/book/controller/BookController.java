package tsvetkov.daniil.book.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsvetkov.daniil.auth.service.BookService;
import tsvetkov.daniil.book.entity.Book;
import tsvetkov.daniil.book.service.BookStatusService;

import java.util.Set;

;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookStatusService bookStatusService;


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
}
