package tsvetkov.daniil.search.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tsvetkov.daniil.search.dto.Author;
import tsvetkov.daniil.search.dto.Book;
import tsvetkov.daniil.search.dto.Category;
import tsvetkov.daniil.search.service.AuthorSearchService;
import tsvetkov.daniil.search.service.BookSearchService;
import tsvetkov.daniil.search.service.CategoryService;

@Service
public class EventConsumer {
    private final BookSearchService bookSearchService;
    private final CategoryService categoryService;
    private final AuthorSearchService authorSearchService;

    @Autowired
    public EventConsumer(BookSearchService bookSearchService, CategoryService categoryService, AuthorSearchService authorSearchService)
    {
        this.bookSearchService = bookSearchService;
        this.categoryService = categoryService;
        this.authorSearchService = authorSearchService;
    }

    @KafkaListener(topics = "book_add_search", groupId = "search-group")
    public void listen(Book bookEvent) throws Exception {
        System.err.println(bookEvent.toString());
       // bookService.save(bookEvent);
    }

    @KafkaListener(topics = "category_add_search", groupId = "search-group")
    public void listen(Category category) {
        categoryService.save(category);
    }

    @KafkaListener(topics = "author_add_search", groupId = "search-group")
    public void listen(Author author) {
        authorSearchService.save(author);
    }


    @KafkaListener(topics = "book_remove_search", groupId = "search-group")
    public void listenRemoveBook(Long id) throws Exception {
        bookSearchService.deleteByIndex(id);
    }

    @KafkaListener(topics = "category_remove_search", groupId = "search-group")
    public void listenRemoveCategory(Long id) throws Exception {
        categoryService.deleteByIndex(id);
    }

    @KafkaListener(topics = "author_remove_search", groupId = "search-group")
    public void listenRemoveAuthor(Long id) throws Exception {
        authorSearchService.deleteByIndex(id);
    }

    @KafkaListener(topics = "add_author_search", groupId = "search-group")
    public void listenRemoveAuthor(Author author) throws Exception {
        authorSearchService.save(author);
    }


}
