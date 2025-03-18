package tsvetkov.daniil.book.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsvetkov.daniil.book.dto.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // TODO переделать, добавить к книге этот метод
    Page<Comment> findByBook_Id(Long bookId, Pageable page);
}
