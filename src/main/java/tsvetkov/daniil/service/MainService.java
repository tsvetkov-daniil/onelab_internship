package tsvetkov.daniil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tsvetkov.daniil.dto.AuthorDTO;
import tsvetkov.daniil.dto.BookDTO;
import tsvetkov.daniil.dto.GenreDTO;
import tsvetkov.daniil.repository.AuthorRepository;
import tsvetkov.daniil.repository.BookRepository;
import tsvetkov.daniil.repository.GenreRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class MainService {
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public MainService(GenreRepository gp, AuthorRepository ap, BookRepository bp) {
        this.genreRepository = gp;
        this.authorRepository = ap;
        this.bookRepository = bp;
    }

    public BookDTO addBook(BookDTO book) {
        if (Objects.nonNull(book.getAuthors())) {
            book.getAuthors().forEach(this::addAuthor);
        }

        if (Objects.nonNull(book.getGenres())) {
            book.getGenres().forEach(this::addGenre);
        }
        return bookRepository.save(book);
    }

    public Optional<BookDTO> getBookById(int id) {
        return bookRepository.findById(id);
    }

    public Set<BookDTO> getAllBooks() {
        return bookRepository.findAll();
    }

    public boolean removeBookById(int id) {
        return bookRepository.removeById(id);
    }

    public GenreDTO addGenre(GenreDTO genre) {
        if (Objects.nonNull(genre) && genreRepository.findByName(genre.getName()).isEmpty()) {
            return genreRepository.save(genre);
        }
        GenreDTO h =genreRepository.findByName(genre.getName()).get();
        genre.setId(h.getId());
        return genre;
    }

    public AuthorDTO addAuthor(AuthorDTO author) {
        if (Objects.nonNull(author) && !authorRepository.isExists(author)) {
            return authorRepository.save(author);
        }
        return null;
    }

    public Set<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Set<GenreDTO> getAllGenres() {
        return genreRepository.findAll();
    }
}
