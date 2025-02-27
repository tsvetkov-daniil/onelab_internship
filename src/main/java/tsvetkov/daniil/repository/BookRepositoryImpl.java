package tsvetkov.daniil.repository;

import org.springframework.stereotype.Repository;
import tsvetkov.daniil.dto.AuthorDTO;
import tsvetkov.daniil.dto.BookDTO;

import java.util.*;

@Repository
public class BookRepositoryImpl implements BookRepository{
    private final Map<Integer, BookDTO> books;

    public BookRepositoryImpl() {
        this.books = new HashMap<>();
    }

    @Override
    public BookDTO save(BookDTO book) {
        if (Objects.nonNull(book) && Objects.nonNull(book.getId()) && books.containsKey(book.getId()))
            return book;

        book.setId(getMaxId() + 1);
        books.put(book.getId(), book);

        return book;
    }

    @Override
    public boolean removeById(int id) {
        if (books.containsKey(id)) {
            books.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Set<BookDTO> findAll() {
        return new HashSet<>(books.values());
    }

    @Override
    public Optional<BookDTO> findById(int id) {
        return Optional.ofNullable(books.get(id));
    }

    private Integer getMaxId() {
        return books.values().stream()
                .max(Comparator.comparingInt(BookDTO::getId))
                .map(BookDTO::getId)
                .orElse(0);
    }
}
