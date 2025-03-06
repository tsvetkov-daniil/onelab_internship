package tsvetkov.daniil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.dto.Book;
import tsvetkov.daniil.dto.Comment;
import tsvetkov.daniil.dto.Reader;
import tsvetkov.daniil.dto.Score;
import tsvetkov.daniil.repository.BookRepository;
import tsvetkov.daniil.repository.CommentRepository;
import tsvetkov.daniil.repository.ReaderRepository;
import tsvetkov.daniil.repository.ScoreRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;
    private final ScoreRepository scoreRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, ReaderRepository readerRepository,
                          BookRepository bookRepository, ScoreRepository scoreRepository) {
        this.commentRepository = commentRepository;
        this.readerRepository = readerRepository;
        this.bookRepository = bookRepository;
        this.scoreRepository = scoreRepository;
    }

    @Transactional
    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    public Set<Comment> findAllByBookId(Long bookId) {
        return commentRepository.findAllByBookId(bookId);
    }

    @Transactional
    public Comment update(Long id, Comment updatedComment) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

        existingComment.setText(updatedComment.getText());
        existingComment.setScore(updatedComment.getScore());

        return commentRepository.save(existingComment);
    }

    @Transactional
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public Comment addReaderAndBookToComment(Long commentId, Long readerId, Long bookId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new RuntimeException("Читатель не найден"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        comment.setReader(reader);
        comment.setBook(book);

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment addScoreToComment(Long commentId, Long scoreId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

        Score score = scoreRepository.findById(scoreId)
                .orElseThrow(() -> new RuntimeException("Оценка не найдена"));

        comment.setScore(score);

        return commentRepository.save(comment);
    }
}
