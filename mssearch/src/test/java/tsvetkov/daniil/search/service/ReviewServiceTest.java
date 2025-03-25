package tsvetkov.daniil.search.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import tsvetkov.daniil.search.entity.Review;
import tsvetkov.daniil.search.entity.Score;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewSearchService;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @BeforeEach
    public void setUp() {
        // Очищаем индекс перед тестом
        elasticsearchOperations.indexOps(IndexCoordinates.of("reviews")).delete();
        elasticsearchOperations.indexOps(IndexCoordinates.of("reviews")).create();

        // Добавляем тестовые данные
        Review review1 = Review.builder()
                .id(1L)
                .text("This book is fantastic")
                .authorId(1L)
                .bookId(1L)
                .score(Score.builder().id(1L).value((byte) 5).build())
                .build();

        Review review2 = Review.builder()
                .id(2L)
                .text("The story was boring")
                .authorId(2L)
                .bookId(2L)
                .score(Score.builder().id(2L).value((byte) 2).build())
                .build();

        elasticsearchOperations.save(review1, IndexCoordinates.of("reviews"));
        elasticsearchOperations.save(review2, IndexCoordinates.of("reviews"));

        // Убеждаемся, что данные проиндексированы
        elasticsearchOperations.indexOps(IndexCoordinates.of("reviews")).refresh();
    }

    @Test
    public void testFindByFuzzyText() {
        // Тестируем поиск с опечаткой
        Set<Review> results = reviewSearchService.findByFuzzyText("boo fan");

        // Проверяем результаты
        assertEquals(1, results.size(), "Должен найти один отзыв");
        Review foundReview = results.iterator().next();
        assertEquals(1L, foundReview.getId(), "ID должен быть 1");
        assertEquals("This book is fantastic", foundReview.getText(), "Текст должен совпадать");
    }
}
