package tsvetkov.daniil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.dto.Book;
import tsvetkov.daniil.dto.Category;
import tsvetkov.daniil.dto.Reader;
import tsvetkov.daniil.repository.BookRepository;
import tsvetkov.daniil.repository.CategoryRepository;
import tsvetkov.daniil.repository.ReaderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final ReaderRepository readerRepository;

    @Autowired
    public BookService(BookRepository bookRepository, CategoryRepository categoryRepository, ReaderRepository readerRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.readerRepository = readerRepository;
    }

    public Book create(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    public Book update(Long id, Book updatedBook) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setDescription(updatedBook.getDescription());
        existingBook.setCoverUrl(updatedBook.getCoverUrl());
        existingBook.setReleaseDate(updatedBook.getReleaseDate());
        existingBook.setLanguage(updatedBook.getLanguage());
        existingBook.setStatus(updatedBook.getStatus());
        existingBook.setUsers(updatedBook.getUsers());
        existingBook.setCategories(updatedBook.getCategories());

        return bookRepository.save(existingBook);
    }

    @Transactional
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public Book addCategoryToBook(Long bookId, Long categoryId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));

        book.getCategories().add(category);
        return bookRepository.save(book);
    }

    @Transactional
    public Book removeCategoryFromBook(Long bookId, Long categoryId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));

        book.getCategories().remove(category);
        return bookRepository.save(book);
    }

    @Transactional
    public Book addUserToBook(Long bookId, Long readerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new RuntimeException("Читатель не найден"));

        book.getUsers().add(reader);
        return bookRepository.save(book);
    }

    @Transactional
    public Book removeUserFromBook(Long bookId, Long readerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new RuntimeException("Читатель не найден"));

        book.getUsers().remove(reader);
        return bookRepository.save(book);
    }
}
