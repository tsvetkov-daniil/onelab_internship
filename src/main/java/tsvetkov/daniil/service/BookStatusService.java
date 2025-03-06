package tsvetkov.daniil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tsvetkov.daniil.dto.BookStatus;
import tsvetkov.daniil.repository.BookStatusRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookStatusService {
    private final BookStatusRepository bookStatusRepository;

    @Autowired
    public BookStatusService(BookStatusRepository bookStatusRepository) {
        this.bookStatusRepository = bookStatusRepository;
    }

    public BookStatus create(BookStatus bookStatus) {
        return bookStatusRepository.save(bookStatus);
    }

    public Optional<BookStatus> getById(Long id) {
        return bookStatusRepository.findById(id);
    }

    public List<BookStatus> getAll() {
        return bookStatusRepository.findAll();
    }


    @Transactional
    public BookStatus update(Long id, BookStatus updatedStatus) {
        BookStatus existingStatus = bookStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Статус книги не найден"));

        existingStatus.setTitle(updatedStatus.getTitle());
        existingStatus.setDescription(updatedStatus.getDescription());

        return bookStatusRepository.save(existingStatus);
    }

    public void delete(Long id) {
        if (!bookStatusRepository.existsById(id)) {
            throw new RuntimeException("Статус книги не найден");
        }
        bookStatusRepository.deleteById(id);
    }
}
