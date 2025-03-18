package tsvetkov.daniil.book.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.dto.Book;
import tsvetkov.daniil.book.dto.BookStatus;
import tsvetkov.daniil.book.dto.Category;
import tsvetkov.daniil.book.event.EventProducer;
import tsvetkov.daniil.book.repository.BookRepository;

import java.util.*;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final EventProducer bookEventProducer;
    private final BookStatusService bookStatusService;


    @Autowired
    public BookService(BookRepository bookRepository, CategoryService categoryService,
                       EventProducer bookEventProducer, BookStatusService bookStatusService) {
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
        this.bookEventProducer = bookEventProducer;
        this.bookStatusService = bookStatusService;
    }

    @Transactional
    public Book save(@Valid Book book) {
        if (Objects.nonNull(book.getId())) {
            Book foundBook = this.findById(book.getId());

            //Статус и дату публикации нельзя изменить
            book.setPublishDate(foundBook.getPublishDate());
            book.setStatus(foundBook.getStatus());
        } else {
            book.setPublishDate(new Date());
            book.setStatus(bookStatusService.getDefaultStatus());
        }

        book.getCategories().forEach(r -> {
            String name = categoryService.findById(r.getId()).getName();
            if (!Objects.equals(name, r.getName()))
                throw new IllegalArgumentException("Категория с id: " + r.getId() + " и именем: " + r.getName() + " не найдена");
        });

        bookEventProducer.addToSearch(book);
        return bookRepository.save(book);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Книга не найдена"));
    }

    public Set<Book> findAll(Integer page, Integer itemsPerPage) {
        return new HashSet<>(bookRepository.findAll(PageRequest.of(page, itemsPerPage))
                .getContent());
    }


    @Transactional
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
        bookEventProducer.removeBookFromSearch(id);
    }

    @Transactional
    public void deleteAll() {
        bookRepository.deleteAll();
    }

    @Transactional
    public Book addCategoryToBook(Long bookId, Long categoryId) {
        Book book = this.findById(bookId);
        Category category = categoryService.findById(categoryId);

        Set<Category> categories = new HashSet<>(book.getCategories());
        categories.add(category);
        book.setCategories(categories);

        bookEventProducer.addToSearch(book);
        return bookRepository.save(book);
    }

    @Transactional
    public Book changeStatus(Long bookId, Long statusId) {
        Book foundBook = this.findById(bookId);
        BookStatus foundStatus = bookStatusService.findById(statusId);

        foundBook.setStatus(foundStatus);

        return bookRepository.save(foundBook);
    }

    @Transactional
    public Book deleteCategoryFromBook(Long bookId, Long categoryId) throws IllegalArgumentException {
        Book book = this.findById(bookId);
        Category category = categoryService.findById(categoryId);

        Set<Category> categories = new HashSet<>(book.getCategories());
        categories.removeIf(c -> c.getName().equals(category.getName()));
        book.setCategories(categories);

        bookEventProducer.addToSearch(book);
        return bookRepository.save(book);
    }


    public Book addAuthorToBook(Long bookId, Long authorId) throws IllegalArgumentException {
        Optional<Book> book1 = bookRepository.findById(bookId);
        Book book = book1.get();
        Set<Long> authors = new HashSet<>(book.getAuthors());
        authors.add(authorId);
        book.setAuthors(authors);

        return bookRepository.save(book);
    }

    public Book deleteAuthorFromBook(Long bookId, Long authorId) throws IllegalArgumentException {
        Book book = this.findById(bookId);

        Set<Long> authors = new HashSet<>(book.getAuthors());
        authors.removeIf(a -> a.equals(authorId));
        book.setAuthors(authors);

        return bookRepository.save(book);
    }
}
