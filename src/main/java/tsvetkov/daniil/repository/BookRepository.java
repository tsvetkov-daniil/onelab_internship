package tsvetkov.daniil.repository;

import tsvetkov.daniil.dto.AuthorDTO;
import tsvetkov.daniil.dto.BookDTO;
import tsvetkov.daniil.dto.GenreDTO;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface BookRepository {
    BookDTO save(BookDTO book);
    boolean removeById(int id);
    Set<BookDTO> findAll();
    Optional<BookDTO> findById(int id);
}
