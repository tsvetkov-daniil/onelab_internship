package tsvetkov.daniil.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class MainReview {
    public static void main(String[] args) {
        SpringApplication.run(MainReview.class, args);
    }
}