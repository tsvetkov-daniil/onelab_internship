package tsvetkov.daniil.search.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import tsvetkov.daniil.search.util.EnhancedCompletion;
import tsvetkov.daniil.search.entity.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Slf4j
@Aspect
@Component
public class AuthorSuggestSetter {



    @Pointcut("execution(* tsvetkov.daniil.search.service.AuthorService.save(tsvetkov.daniil.search.dto.Author))")
    public void saveAuthor() {
    }

    @Before("save()")
    public void save(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof Author author) {
            setSuggests(author);
        }
    }


    private void setSuggests(Author author) {
        if (author == null) return;

        final String lastName = Objects.requireNonNullElse(author.getLastName(), "");
        final String firstName = Objects.requireNonNullElse(author.getFirstName(), "");
        final String middleName = Objects.requireNonNullElse(author.getMiddleName(), "");

        String[] fullNamePermutations = generateFullNamePermutations(lastName, firstName, middleName);
        if (fullNamePermutations.length > 0) {
            updateIfChanged(author::setFullNameSuggest, author.getFullNameSuggest(), fullNamePermutations);
        }

        String nickname = Objects.requireNonNullElse(author.getNickname(), "");
        if (!nickname.isEmpty()) {
            updateIfChanged(author::setNicknameSuggest, author.getNicknameSuggest(), nickname);
        }
    }

    private void updateIfChanged(Consumer<EnhancedCompletion> setter, EnhancedCompletion current, String... newValue) {
        EnhancedCompletion newSuggest = new EnhancedCompletion(newValue);
        if (!newSuggest.equals(current)) {
            setter.accept(newSuggest);
        }
    }

    private String[] generateFullNamePermutations(String lastName, String firstName, String middleName) {
        List<String> parts = Stream.of(lastName, firstName, middleName)
                .filter(s -> !s.isBlank())
                .toList();

        if (parts.isEmpty()) {
            return new String[0];
        }

        List<String> permutations = new ArrayList<>();
        if (parts.size() == 1) {
            permutations.add(parts.get(0));
        } else if (parts.size() == 2) {
            permutations.add(String.join(" ", parts.get(0), parts.get(1)));
            permutations.add(String.join(" ", parts.get(1), parts.get(0)));
        } else {
            permutations.add(String.join(" ", parts.get(0), parts.get(1), parts.get(2)));
            permutations.add(String.join(" ", parts.get(0), parts.get(2), parts.get(1)));
            permutations.add(String.join(" ", parts.get(1), parts.get(0), parts.get(2)));
            permutations.add(String.join(" ", parts.get(1), parts.get(2), parts.get(0)));
            permutations.add(String.join(" ", parts.get(2), parts.get(0), parts.get(1)));
            permutations.add(String.join(" ", parts.get(2), parts.get(1), parts.get(0)));
        }

        return permutations.toArray(new String[0]);
    }
}
