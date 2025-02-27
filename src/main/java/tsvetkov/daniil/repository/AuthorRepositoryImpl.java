package tsvetkov.daniil.repository;

import org.springframework.stereotype.Repository;
import tsvetkov.daniil.dto.AuthorDTO;

import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {
    private final Map<Integer, AuthorDTO> authors;

    public AuthorRepositoryImpl() {
        this.authors = new HashMap<>();
    }

    @Override
    public AuthorDTO save(AuthorDTO author) {
        if (Objects.nonNull(author) && Objects.nonNull(author.getId()) && authors.containsKey(author.getId()))
            return author;

        author.setId(getMaxId() + 1);
        authors.put(author.getId(), author);

        return author;
    }

    @Override
    public boolean removeById(int id) {
        return Objects.nonNull(authors.remove(id));
    }

    @Override
    public Set<AuthorDTO> findAll() {
        return new HashSet<>(authors.values());
    }

    @Override
    public Optional<AuthorDTO> findById(int id) {
        return Optional.ofNullable(authors.get(id));
    }

    @Override
    public boolean isExists(AuthorDTO author) {
        return authors.containsValue(author);
    }

    private Integer getMaxId() {
        return authors.values().stream()
                .max(Comparator.comparingInt(AuthorDTO::getId))
                .map(AuthorDTO::getId)
                .orElse(0);
    }
}
