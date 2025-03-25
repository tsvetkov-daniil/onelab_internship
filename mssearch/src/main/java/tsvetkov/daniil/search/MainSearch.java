package tsvetkov.daniil.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@SpringBootApplication
@EnableElasticsearchRepositories
@EnableAspectJAutoProxy
public class MainSearch {
    public static void main(String[] args) {
       SpringApplication.run(MainSearch.class, args);
    }

}