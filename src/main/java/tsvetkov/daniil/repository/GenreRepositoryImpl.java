package tsvetkov.daniil.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.dto.GenreDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class GenreRepositoryImpl implements GenreRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GenreDTO save(GenreDTO genre) {
        if (Objects.isNull(genre))
            return getDefault();

        Optional<GenreDTO> genreDb = findByName(genre.getName());

        if (genreDb.isPresent())
        {
            genre.setId(genreDb.get().getId());
            return genre;
        }


        String sql = "INSERT INTO genre (name, description) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = 0;

        try {
            rowsAffected = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, genre.getName());
                ps.setString(2, genre.getDescription());
                return ps;
            }, keyHolder);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        if (rowsAffected > 0 && keyHolder.getKey() != null) {
            genre.setId(keyHolder.getKey().intValue());
            return genre;
        }
        return null;
    }

    @Override
    public boolean removeById(int id) {
        String sql = "DELETE FROM genre WHERE id = ?";
        try {
            return jdbcTemplate.update(sql, id) > 0;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }

    }

    @Override
    public Optional<GenreDTO> findById(int id) {
        String sql = "SELECT * FROM genre WHERE id = ?";
        try {
            GenreDTO genre = jdbcTemplate.queryForObject(sql, new GenreRowMapper(), id);
            return Optional.ofNullable(genre);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    @Override
    public Set<GenreDTO> findAll() {
        Set<GenreDTO> result = new HashSet<>();
        try {
            result.addAll(jdbcTemplate.query("SELECT * FROM genre", new GenreRowMapper()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<GenreDTO> findByName(String name) {
        try {
            GenreDTO genre = jdbcTemplate.queryForObject(
                    "SELECT * FROM genre WHERE name = ?",
                    new GenreRowMapper(),
                    name
            );
            return Optional.ofNullable(genre);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<GenreDTO> findGenresByBookId(int bookId) {
        String sql = "SELECT * FROM genre g JOIN book_genre bg ON g.id = bg.genre_id WHERE bg.book_id = ?";
        return jdbcTemplate.query(sql, new GenreRowMapper(), bookId);
    }

    private static class GenreRowMapper implements RowMapper<GenreDTO> {
        @Override
        public GenreDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            GenreDTO genre = GenreDTO.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .build();
            return genre;
        }
    }

    private GenreDTO getDefault() {
        Optional<GenreDTO> genre = findByName("Uncategorized");
        if (genre.isPresent())
            return genre.get();

        throw new NoSuchElementException();
    }
}
