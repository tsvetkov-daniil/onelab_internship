package tsvetkov.daniil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.dto.BookStatus;

@Repository
public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {
}
