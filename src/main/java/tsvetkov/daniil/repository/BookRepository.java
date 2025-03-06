package tsvetkov.daniil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.dto.Book;

import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
