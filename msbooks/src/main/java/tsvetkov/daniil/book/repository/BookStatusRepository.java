package tsvetkov.daniil.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.book.dto.BookStatus;

import java.util.Optional;

@Repository
public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {
    Optional<BookStatus> findByTitle(String name);
}
