package tsvetkov.daniil.book.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsvetkov.daniil.book.entity.BookStatus;
import tsvetkov.daniil.book.service.BookStatusService;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class StatusController {
    private final BookStatusService bookStatusService;

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
}
