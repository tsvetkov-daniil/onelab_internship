package tsvetkov.daniil.repository;

import org.springframework.stereotype.Repository;
import tsvetkov.daniil.Main;
import tsvetkov.daniil.dto.AuthorDTO;
import tsvetkov.daniil.dto.GenreDTO;

import java.util.*;

@Repository
public class GenreRepositoryImpl implements GenreRepository {
    private final Map<Integer, GenreDTO> genres;

    public GenreRepositoryImpl() {
        this.genres = new HashMap<>();
    }

    @Override
    public GenreDTO save(GenreDTO genre) {
        if (Objects.nonNull(genre) && Objects.nonNull(genre.getId()) && genres.containsKey(genre.getId()))
            return genre;

        genre.setId(getMaxId() + 1);
        genres.put(genre.getId(), genre);
        return genre;
    }

    @Override
    public boolean removeById(int id) {
        if (genres.containsKey(id)) {
            genres.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<GenreDTO> findById(int id) {
        return Optional.ofNullable(genres.get(id));
    }

    @Override
    public Set<GenreDTO> findAll() {
        return new HashSet<>(genres.values());
    }

    @Override
    public Optional<GenreDTO> findByName(String name) {
        return genres.values().stream()
                .filter(genre -> genre.getName().equals(name))
                .findFirst();
    }

    private Integer getMaxId() {
        return genres.values().stream()
                .max(Comparator.comparingInt(GenreDTO::getId))
                .map(GenreDTO::getId)
                .orElse(0);
    }
}
