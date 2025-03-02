package tsvetkov.daniil.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.dto.AuthorDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public AuthorDTO save(AuthorDTO author) {
        String sql = "INSERT INTO author (first_name, last_name, pseudonym) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = 0;

        try {
            rowsAffected = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, author.getFirstName());
                ps.setString(2, author.getLastName());
                ps.setString(3, author.getPseudonym());
                return ps;
            }, keyHolder);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        if (rowsAffected > 0 && Objects.nonNull(keyHolder.getKey())) {
            author.setId(keyHolder.getKey().intValue());
            return author;
        }
        return null;
    }

    @Override
    public boolean removeById(int id) {
        String sql = "DELETE FROM author WHERE id = ?";
        try {
            return jdbcTemplate.update(sql, id) > 0;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Set<AuthorDTO> findAll() {
        Set<AuthorDTO> result = new HashSet<>();
        try {
            result.addAll(jdbcTemplate.query("SELECT * FROM author", new AuthorRowMapper()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<AuthorDTO> findById(int id) {
        String sql = "SELECT * FROM genre WHERE id = ?";
        try {
            AuthorDTO author = jdbcTemplate.queryForObject(sql, new AuthorRowMapper(), id);
            return Optional.ofNullable(author);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isExists(AuthorDTO author) {
        if (Objects.isNull(author)) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM author WHERE first_name = ? AND last_name = ? AND pseudonym = ?";

        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
                    author.getFirstName(),
                    author.getLastName(),
                    author.getPseudonym());

            return count != null && count > 0;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<AuthorDTO> findAuthorsByBookId(int bookId) {
        String sql = "SELECT * FROM author a JOIN book_author ba ON a.id = ba.author_id WHERE ba.book_id = ?";
        return jdbcTemplate.query(sql, new AuthorRowMapper(), bookId);
    }

    private static class AuthorRowMapper implements RowMapper<AuthorDTO> {
        @Override
        public AuthorDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return AuthorDTO.builder()
                    .id(rs.getInt("id"))
                    .firstName(rs.getString("firstName"))
                    .lastName(rs.getString("lastName"))
                    .pseudonym(rs.getString("pseudonym"))
                    .build();
        }
    }
}
