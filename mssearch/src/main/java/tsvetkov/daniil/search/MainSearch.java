package tsvetkov.daniil.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@SpringBootApplication
@EnableElasticsearchRepositories
@EnableAspectJAutoProxy
public class MainSearch {
    private static ApplicationContext ctx;
    public static void main(String[] args) {
        ctx = SpringApplication.run(MainSearch.class, args);
    }

}