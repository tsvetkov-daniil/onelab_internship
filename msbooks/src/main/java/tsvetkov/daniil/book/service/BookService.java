package tsvetkov.daniil.auth.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tsvetkov.daniil.book.entity.Book;
import tsvetkov.daniil.book.entity.BookStatus;
import tsvetkov.daniil.book.entity.Category;
import tsvetkov.daniil.book.event.BookEventProducer;
import tsvetkov.daniil.book.exception.AuthorNotFoundException;
import tsvetkov.daniil.book.exception.BookNotFoundException;
import tsvetkov.daniil.book.exception.BookStatusNotFoundException;
import tsvetkov.daniil.book.exception.CategoryNotFoundException;
import tsvetkov.daniil.book.repository.BookRepository;
import tsvetkov.daniil.book.service.BookStatusService;
import tsvetkov.daniil.book.service.CategoryService;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

@AllArgsConstructor
@Service
@Validated
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final BookEventProducer bookEventProducer;
    private final BookStatusService bookStatusService;

    @Transactional
    public Book save(@Valid Book book) {
        if (Objects.nonNull(book.getId())) {
            Book existingBook = findById(book.getId());
            book.setPublishDate(existingBook.getPublishDate());
            book.setStatus(existingBook.getStatus());
        } else {
            book.setPublishDate(new Date());
            book.setStatus(bookStatusService.getDefaultStatus());
        }

        validateCategories(book.getCategories());
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Set<Book> findAll(Integer page, Integer itemsPerPage) {
        return new HashSet<>(bookRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent());
    }

    @Transactional
    public void deleteById(Long id) {
        findById(id);
        bookRepository.deleteById(id);
        bookEventProducer.deleteBookFromSearch(id);
    }

    @Transactional
    public void deleteAll() {
        bookRepository.deleteAll();
    }

    @Transactional
    public Book addCategoryToBook(Long bookId, Long categoryId) {
        Book book = findById(bookId);
        Category category = categoryService.findById(categoryId);

        Set<Category> categories = new HashSet<>(book.getCategories());
        categories.add(category);
        book.setCategories(categories);

        Book updatedBook = bookRepository.save(book);
        bookEventProducer.addBookToSearch(updatedBook);
        return updatedBook;
    }

    @Transactional
    public Book changeStatus(Long bookId, Long statusId) {
        Book book = findById(bookId);
        BookStatus status = bookStatusService.findById(statusId);

        book.setStatus(status);
        Book updatedBook = bookRepository.save(book);
        if (book.getStatus().getTitle().equals(BookStatusService.DEFAULT_STATUS_NAME)) {
            bookEventProducer.deleteBookFromSearch(book.getId());
        }else {
            bookEventProducer.addBookToSearch(updatedBook);
        }
        return updatedBook;
    }

    @Transactional
    public Book deleteCategoryFromBook(Long bookId, Long categoryId) {
        Book book = findById(bookId);
        Category category = categoryService.findById(categoryId);

        Set<Category> categories = new HashSet<>(book.getCategories());
        if (!categories.removeIf(c -> c.getId().equals(category.getId()))) {
            throw new CategoryNotFoundException();
        }
        book.setCategories(categories);

        Book updatedBook = bookRepository.save(book);
        bookEventProducer.addBookToSearch(updatedBook);
        return updatedBook;
    }

    @Transactional
    public Book addAuthorToBook(Long bookId, Long authorId) {
        Book book = findById(bookId);
        Set<Long> authors = new HashSet<>(book.getAuthors());
        authors.add(authorId);
        book.setAuthors(authors);
        Book updatedBook = bookRepository.save(book);
        bookEventProducer.addBookToSearch(updatedBook);
        return updatedBook;
    }

    @Transactional
    public Book deleteAuthorFromBook(Long bookId, Long authorId) {
        Book book = findById(bookId);
        Set<Long> authors = new HashSet<>(book.getAuthors());
        if (!authors.remove(authorId)) {
            throw new AuthorNotFoundException();
        }
        book.setAuthors(authors);
        return bookRepository.save(book);
    }

    @Transactional
    public void incrementTotalComments(Long bookId) {
        updateCounter(bookId, book -> book.setTotalComments(book.getTotalComments() + 1));
    }

    @Transactional
    public void decrementTotalComments(Long bookId) {
        updateCounter(bookId, book -> book.setTotalComments(book.getTotalComments() - 1));
    }

    @Transactional
    public void incrementTotalReviews(Long bookId) {
        updateCounter(bookId, book -> book.setTotalReviews(book.getTotalReviews() + 1));
    }

    @Transactional
    public void decrementTotalReviews(Long bookId) {
        updateCounter(bookId, book -> book.setTotalReviews(book.getTotalReviews() - 1));
    }

    @Transactional
    public void approveBook(Long bookId) {
        Book book = findById(bookId);
        BookStatus approvedStatus = bookStatusService.findByTitle("APROOVE")
                .orElseThrow(BookStatusNotFoundException::new);
        book.setStatus(approvedStatus);
        bookEventProducer.addBookToSearch(bookRepository.save(book));
    }

    @Transactional
    public void mBook(Long bookId) {
        Book book = findById(bookId);
        BookStatus approvedStatus = bookStatusService.findByTitle(BookStatusService.DEFAULT_STATUS_NAME)
                .orElseThrow(BookStatusNotFoundException::new);
        book.setStatus(approvedStatus);
        bookEventProducer.addBookToSearch(bookRepository.save(book));
    }

    private void validateCategories(Set<Category> categories) {
        categories.forEach(category -> {
            Category found = categoryService.findById(category.getId());
            if (!found.getName().equals(category.getName())) {
                throw new CategoryNotFoundException();
            }
        });
    }

    private void updateCounter(Long bookId, Consumer<Book> updater) {
        Book book = findById(bookId);
        updater.accept(book);
        bookRepository.save(book);
    }
}