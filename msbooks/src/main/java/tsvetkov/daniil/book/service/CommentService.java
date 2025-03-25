package tsvetkov.daniil.book.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tsvetkov.daniil.book.entity.Comment;
import tsvetkov.daniil.book.exception.CommentNotFoundException;
import tsvetkov.daniil.book.repository.CommentRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Validated
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Comment save(@Valid Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(CommentNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Set<Comment> findAllByBookId(Long bookId, Integer pageNumber, Integer pageSize) {
        return new HashSet<>(commentRepository.findByBook_Id(bookId, PageRequest.of(pageNumber, pageSize)).getContent());
    }

    @Transactional
    public void deleteById(Long id) {
        findById(id);
        commentRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        commentRepository.deleteAll();
    }
}