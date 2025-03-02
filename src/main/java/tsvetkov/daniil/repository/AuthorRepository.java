package tsvetkov.daniil.repository;

import tsvetkov.daniil.dto.AuthorDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorRepository {
    AuthorDTO save(AuthorDTO author);
    boolean removeById(int id);
    Set<AuthorDTO> findAll();
    Optional<AuthorDTO> findById(int id);
    boolean isExists(AuthorDTO author);
    List<AuthorDTO> findAuthorsByBookId(int bookId);
}
