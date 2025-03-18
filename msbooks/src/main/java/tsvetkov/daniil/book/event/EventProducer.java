package tsvetkov.daniil.book.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tsvetkov.daniil.book.dto.Book;
import tsvetkov.daniil.book.dto.Category;

@Service
public class EventProducer {

    private final KafkaTemplate<String, Book> bookProducer;
    private final KafkaTemplate<String, Long> idProducer;
    private final KafkaTemplate<String, Category> categoryProducer;

    @Autowired
    public EventProducer(KafkaTemplate<String, Book> kafkaTemplate, KafkaTemplate<String, Long> idProducer, KafkaTemplate<String, Category> categoryProducer) {
        this.bookProducer = kafkaTemplate;
        this.idProducer = idProducer;
        this.categoryProducer = categoryProducer;
    }

    public void addToSearch(Book book) {
        bookProducer.send("book_add_search", book);
    }

    public void addToSearch(Category bookEvent) {
        categoryProducer.send("category_add_search", bookEvent);
    }

    public void createAuthor(Long id) {
        idProducer.send("book_remove_search", id);
    }
    public void removeBookFromSearch(Long id) {
        idProducer.send("book_remove_search", id);
    }

    public void removeCatFromSearch(Long id) {
        idProducer.send("category_remove_search", id);
    }


}
