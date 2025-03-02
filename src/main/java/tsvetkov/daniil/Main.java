package tsvetkov.daniil;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import tsvetkov.daniil.dto.AuthorDTO;
import tsvetkov.daniil.dto.BookDTO;
import tsvetkov.daniil.dto.GenreDTO;
import tsvetkov.daniil.service.MainService;

import java.util.Set;


@ComponentScan
@EnableAspectJAutoProxy
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

        MainService mainService = context.getBean(MainService.class);

        // Создаем жанры
        GenreDTO genre1 = GenreDTO.builder()
                .name("Fantasy")
                .build();
        GenreDTO genre2 = GenreDTO.builder()
                .name("Science Fiction")
                .build();

        // Добавляем жанры
        mainService.addGenre(genre1);
        mainService.addGenre(genre2);

        // Создаем авторов
        AuthorDTO author1 = AuthorDTO.builder()
                .pseudonym("J.R.R. Tolkien")
                .build();

        AuthorDTO author2 = AuthorDTO.builder()
                .firstName("Isaac")
                .lastName("Asimov")
                .build();

        // Добавляем авторов
        mainService.addAuthor(author1);
        mainService.addAuthor(author2);

        // Создаем книгу
        BookDTO book = BookDTO.builder()
                .authors(Set.of(author1))
                .genres(Set.of(genre1))
                .title("Hobbit")
                .build();

        // Добавляем книгу
        BookDTO addedBook = mainService.addBook(book);

        // Проверяем, была ли книга добавлена
        System.out.println("Книга добавлена: " + addedBook);

        // Получаем все книги
        System.out.println("Все книги:");
        mainService.getAllBooks().forEach(System.out::println);

        //Проверяем удалилась ли книга
        mainService.removeBookById(1);
        System.out.println("Проверка удаления:");
        mainService.getAllBooks().forEach(System.out::println);

        //Проверяем были ли добавлены авторы и жанры
        System.out.println("Проверка авторов и жанров:");
        mainService.getAllAuthors().forEach(System.out::println);
        mainService.getAllGenres().forEach(System.out::println);

        context.close();

    }
}