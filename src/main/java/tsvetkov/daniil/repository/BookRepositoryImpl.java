package tsvetkov.daniil.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.dto.AuthorDTO;
import tsvetkov.daniil.dto.BookDTO;
import tsvetkov.daniil.dto.GenreDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private final JdbcTemplate jdbcTemplate;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Autowired
    public BookRepositoryImpl(JdbcTemplate jdbcTemplate, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public BookDTO save(BookDTO book) {
        if(Objects.isNull(book))
            return null;

        String sql = "INSERT INTO book (title, description) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = 0;

        try {
            rowsAffected = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, book.getTitle());
                ps.setString(2, book.getDescription());
                return ps;
            }, keyHolder);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        if (rowsAffected > 0 && keyHolder.getKey() != null) {
            int bookId = keyHolder.getKey().intValue();
            book.setId(bookId);

            if (book.getAuthors() != null) {
                for (AuthorDTO author : book.getAuthors()) {
                    jdbcTemplate.update("INSERT INTO book_author (book_id, author_id) VALUES (?, ?)", bookId, author.getId());
                }
            }

            if (book.getGenres() != null) {
                for (GenreDTO genre : book.getGenres()) {
                    jdbcTemplate.update("INSERT INTO book_genre (book_id, genre_id) VALUES (?, ?)", bookId, genre.getId());
                }
            }
            return book;
        }
        return null;
    }

    @Override
    public boolean removeById(int id) {
        try {
            jdbcTemplate.update("DELETE FROM book_author WHERE book_id = ?", id);
            jdbcTemplate.update("DELETE FROM book_genre WHERE book_id = ?", id);

            return jdbcTemplate.update("DELETE FROM book WHERE id = ?", id) > 0;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<BookDTO> findById(int id) {
        String sql = "SELECT * FROM book WHERE id = ?";
        try {
            BookDTO book = jdbcTemplate.queryForObject(sql, new BookRowMapper(), id);
            return Optional.ofNullable(book);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Set<BookDTO> findAll() {
        Set<BookDTO> books = new HashSet<>();
        try {
            books.addAll(jdbcTemplate.query("SELECT * FROM book", new BookRowMapper()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return books;
    }

    private class BookRowMapper implements RowMapper<BookDTO> {
        @Override
        public BookDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            int bookId = rs.getInt("id");
            BookDTO book = BookDTO.builder()
                    .id(bookId)
                    .title(rs.getString("title"))
                    .description(rs.getString("description"))
                    .build();

            book.setAuthors(new HashSet<>(authorRepository.findAuthorsByBookId(bookId)));
            book.setGenres(new HashSet<>(genreRepository.findGenresByBookId(bookId)));

            return book;
        }
    }
}
