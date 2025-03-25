package tsvetkov.daniil.book.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tsvetkov.daniil.book.entity.BookStatus;
import tsvetkov.daniil.book.exception.BookStatusNotFoundException;
import tsvetkov.daniil.book.repository.BookStatusRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Validated
public class BookStatusService {

    public static final String DEFAULT_STATUS_NAME = "На модерации";
    private static final String DEFAULT_STATUS_DESCRIPTION = "Книга не опубликована, ожидает одобрения модерации";

    private final BookStatusRepository bookStatusRepository;

    @Transactional
    public BookStatus save(@Valid BookStatus bookStatus) {
        return bookStatusRepository.save(bookStatus);
    }
    @Transactional(readOnly = true)
    public BookStatus findById(Long id) {
        return bookStatusRepository.findById(id)
                .orElseThrow(BookStatusNotFoundException::new);
    }
    @Transactional(readOnly = true)
    public Set<BookStatus> findAll(Integer pageNumber, Integer pageSize) {
        return new HashSet<>(bookStatusRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent());
    }

    @Transactional
    public void deleteById(Long id) {
        BookStatus status = findById(id);
        bookStatusRepository.delete(status);
    }

    @Transactional
    public void deleteAll() {
        bookStatusRepository.deleteAll();
    }
    @Transactional(readOnly = true)
    public Optional<BookStatus> findByTitle(String title) {
        return bookStatusRepository.findByTitle(title);
    }

    // TODO ddd
    @Transactional
    public BookStatus getDefaultStatus() {
        return findByTitle(DEFAULT_STATUS_NAME)
                .orElseGet(() -> bookStatusRepository.save(BookStatus.builder()
                        .title(DEFAULT_STATUS_NAME)
                        .description(DEFAULT_STATUS_DESCRIPTION)
                        .build()));
    }


}
