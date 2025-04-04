package tsvetkov.daniil.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.book.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
