package tsvetkov.daniil.book.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tsvetkov.daniil.book.entity.Book;
import tsvetkov.daniil.dto.BookDTO;
import tsvetkov.daniil.util.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class BookEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Mapper mapper;


    public void addBookToSearch(Book book) {
        BookDTO bookDTO = mapper.map(book, BookDTO.class);
        kafkaTemplate.send("book-create-search", bookDTO);
    }

    public void deleteBookFromSearch(Long bookId) {
        kafkaTemplate.send("book-delete-search", bookId);
    }
}
