package tsvetkov.daniil.search.service;


import co.elastic.clients.elasticsearch.core.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tsvetkov.daniil.search.dto.Book;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final BookSearchService bookService;
    private final AuthorSearchService authorService;
    private final CategoryService categoryService;

    public Map<String, List<?>> search(SearchRequest request) throws IOException {
        List<Book> books = bookService.searchAcrossAllFields(request.q(),1, 5);
        List<String> authors = authorService.suggestAuthorNames(request.q(), 5);
        List<String> categories = categoryService.suggestCategories(request.q(), 5);

        return Map.of(
                "books", books,
                "authors", authors,
                "categories", categories
        );
    }
}
