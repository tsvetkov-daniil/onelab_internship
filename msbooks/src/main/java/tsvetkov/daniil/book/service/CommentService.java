package tsvetkov.daniil.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.dto.Comment;
import tsvetkov.daniil.book.repository.CommentRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий с id: " + id + "не найден"));
    }

    public Set<Comment> findAllByBookId(Long bookId, Integer pageNumber, Integer pageSize) {
        return new HashSet<>(commentRepository.findByBook_Id(bookId, PageRequest.of(pageNumber, pageSize)).getContent());
    }


    @Transactional
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        commentRepository.deleteAll();
    }

}
