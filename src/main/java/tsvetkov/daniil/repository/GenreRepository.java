package tsvetkov.daniil.repository;

import tsvetkov.daniil.dto.GenreDTO;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository {
    GenreDTO save(GenreDTO genre);
    boolean removeById(int id);
    Optional<GenreDTO> findById(int id);
    Set<GenreDTO> findAll();
    Optional<GenreDTO> findByName(String name);
}
