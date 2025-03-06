package servicetest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tsvetkov.daniil.dto.*;
import tsvetkov.daniil.repository.BookRepository;
import tsvetkov.daniil.repository.CategoryRepository;
import tsvetkov.daniil.repository.ReaderRepository;
import tsvetkov.daniil.service.BookService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ReaderRepository readerRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void testCreateBook() {
        BookStatus bookStatus = BookStatus.builder()
                .id(0L)
                .title("Availibale")
                .description("You can buy it")
                .build();

        Book book = Book.builder()
                .id(1L)
                .title("Test Book")
                .description("Description")
                .coverUrl("http://example.com")
                .releaseDate(Date.valueOf(LocalDate.now()))
                .language(Language.builder().id(1L).build())
                .status(bookStatus)
                .users(new HashSet<>())
                .categories(new HashSet<>())
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book savedBook = bookService.create(book);
        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
    }

    @Test
    public void testFindById() {
        BookStatus bookStatus = BookStatus.builder()
                .id(0L)
                .title("Availibale")
                .description("You can buy it")
                .build();

        Book book = Book.builder()
                .id(1L)
                .title("Test Book")
                .description("Description")
                .coverUrl("http://example.com")
                .releaseDate(Date.valueOf(LocalDate.now()))
                .language(Language.builder().id(1L).build())
                .status(bookStatus)
                .users(new HashSet<>())
                .categories(new HashSet<>())
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = bookService.findById(1L);
        assertTrue(foundBook.isPresent());
        assertEquals("Test Book", foundBook.get().getTitle());
    }

    @Test
    public void testFindAll() {
        List<Book> books = Arrays.asList(
                Book.builder().id(1L).title("Book 1").build(),
                Book.builder().id(2L).title("Book 2").build()
        );

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> foundBooks = bookService.findAll();
        assertNotNull(foundBooks);
        assertEquals(2, foundBooks.size());
    }

    @Test
    public void testUpdateBook() {
        BookStatus bookStatus = BookStatus.builder()
                .id(0L)
                .title("Availibale")
                .description("You can buy it")
                .build();

        Book existingBook = Book.builder()
                .id(1L)
                .title("Old Title")
                .description("Description")
                .coverUrl("http://example.com")
                .releaseDate(Date.valueOf(LocalDate.now()))
                .language(Language.builder().id(1L).build())
                .status(bookStatus)
                .users(new HashSet<>())
                .categories(new HashSet<>())
                .build();

        bookStatus.setTitle("Unavailable");

        Book updatedBook = Book.builder()
                .id(1L)
                .title("Updated Title")
                .description("Updated Description")
                .coverUrl("http://example.com")
                .releaseDate(Date.valueOf(LocalDate.now()))
                .language(Language.builder().id(1L).build())
                .status(bookStatus)
                .users(new HashSet<>())
                .categories(new HashSet<>())
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        Book result = bookService.update(1L, updatedBook);
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    void testDeleteBook() {
        Long bookId = 1L;
        doNothing().when(bookRepository).deleteById(bookId);

        bookService.delete(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    public void testAddCategoryToBook() {
        Book book = Book.builder()
                .id(1L)
                .title("Test Book")
                .categories(new HashSet<>())
                .build();

        Category category = Category.builder()
                .id(1L)
                .name("Fiction")
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.addCategoryToBook(1L, 1L);
        assertNotNull(result);
        assertTrue(result.getCategories().contains(category));
    }

    @Test
    public void testRemoveCategoryFromBook() {
        Category category = Category.builder()
                .id(1L)
                .name("Fiction")
                .build();

        Book book = Book.builder()
                .id(1L)
                .title("Test Book")
                .categories(new HashSet<>(Arrays.asList(category)))
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.removeCategoryFromBook(1L, 1L);
        assertNotNull(result);
        assertFalse(result.getCategories().contains(category));
    }

    @Test
    public void testAddUserToBook() {
        Reader reader = Reader.builder()
                .id(1L)
                .firstName("John")
                .build();

        Book book = Book.builder()
                .id(1L)
                .title("Test Book")
                .users(new HashSet<>())
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.addUserToBook(1L, 1L);
        assertNotNull(result);
        assertTrue(result.getUsers().contains(reader));
    }

    @Test
    public void testRemoveUserFromBook() {
        Reader reader = Reader.builder()
                .id(1L)
                .firstName("John")
                .build();

        Book book = Book.builder()
                .id(1L)
                .title("Test Book")
                .users(new HashSet<>(Arrays.asList(reader)))
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.removeUserFromBook(1L, 1L);
        assertNotNull(result);
        assertFalse(result.getUsers().contains(reader));
    }
}
