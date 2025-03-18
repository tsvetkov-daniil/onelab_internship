package tsvetkov.daniil.book.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.book.dto.BookStatus;
import tsvetkov.daniil.book.repository.BookStatusRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class BookStatusService {
    private final BookStatusRepository bookStatusRepository;

    @Autowired
    public BookStatusService(BookStatusRepository bookStatusRepository) {
        this.bookStatusRepository = bookStatusRepository;
    }

    @Transactional
    public BookStatus save(@Valid BookStatus bookStatus) {
        return bookStatusRepository.save(bookStatus);
    }

    public BookStatus findById(Long id) {
        return bookStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Статус с таким id не найден"));
    }

    public Set<BookStatus> findAll(Integer pageNumber, Integer pageSize) {
        return new HashSet<>(bookStatusRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent());
    }


    @Transactional
    public void deleteById(Long id) {
        if (!bookStatusRepository.existsById(id)) {
            throw new IllegalArgumentException("Статус книги не найден");
        }
        bookStatusRepository.deleteById(id);
    }

    public Optional<BookStatus> findByTitle(String name) {
        return bookStatusRepository.findByTitle(name);
    }

    @Transactional
    public void deleteAll() {
        bookStatusRepository.deleteAll();
    }

    @Transactional
    public BookStatus getDefaultStatus() {
        String defaultStatusName = "Added";
        return this.findByTitle(defaultStatusName)
                .orElseGet(() -> bookStatusRepository.save(BookStatus.builder()
                        .title(defaultStatusName)
                        .description("Книга опубликована")
                        .build()));
    }
}
