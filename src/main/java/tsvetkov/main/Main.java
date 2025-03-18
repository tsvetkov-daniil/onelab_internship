package tsvetkov.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import tsvetkov.daniil.auth.MainAuth;
import tsvetkov.daniil.auth.dto.Reader;
import tsvetkov.daniil.auth.service.ReaderService;
import tsvetkov.daniil.book.MainBook;
import tsvetkov.daniil.book.dto.Book;
import tsvetkov.daniil.book.dto.Category;
import tsvetkov.daniil.book.service.BookService;
import tsvetkov.daniil.book.service.CategoryService;

import java.util.Date;
import java.util.Set;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws Exception {

        ApplicationContext ctxAuth = null;
        while (ctxAuth == null) {
            ctxAuth = new SpringApplicationBuilder()
                    .sources(MainAuth.class)  // Главный класс Spring Boot приложения
                    .properties("spring.config.location=classpath:application.properties")
                    .run(args);
        }
        ApplicationContext ctxBook = null;
        while (ctxBook == null) {
            ctxBook = new SpringApplicationBuilder()
                    .sources(MainBook.class)  // Главный класс Spring Boot приложения
                    .properties("spring.config.location=classpath:application.properties")
                    .run(args);
        }
        Reader reader = Reader.builder()
                .firstName("Maksim")
                .lastName("Ivanovich")
                .email("287y382h@gmail.com")
                .familyName("Ivanovich")
                .nickname("booklover")
                .build();

        reader = ctxAuth.getBean(ReaderService.class).save(reader);

        Category category = Category.builder()
                .name("Scince")
                .build();

        category = ctxBook.getBean(CategoryService.class).save(category);
        Book book = Book.builder()
                .categories(Set.of(category))
                .authors(Set.of(reader.getId()))
                .price(29f)
                .publishDate(new Date())
                .description("Some")
                .title("Some book")
                .build();

        System.err.println(book);
        ctxBook.getBean(BookService.class).save(book);

    }
}
