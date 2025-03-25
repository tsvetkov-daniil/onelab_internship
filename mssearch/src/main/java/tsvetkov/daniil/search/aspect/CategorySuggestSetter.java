package tsvetkov.daniil.search.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import tsvetkov.daniil.search.entity.Category;

@Aspect
public class CategorySuggestSetter {

    @Pointcut("execution(* tsvetkov.daniil.search.dto.Category$CategoryBuilder.name(String))")
    public void setNameViaBuilder() {
    }

    @Pointcut("execution(* tsvetkov.daniil.dto.Category.setName(String))")
    public void setName() {
    }


    @Around("setNameViaBuilder() || setName()")
    public void setNameSuggest(ProceedingJoinPoint joinPoint) throws Throwable {
        final Object obj = joinPoint.proceed();
        if (obj instanceof Category category)
            nameSuggestSetter(category);
        else if(joinPoint.getTarget() instanceof Category category)
            nameSuggestSetter(category);
    }

    private void nameSuggestSetter(Category category) {
        final String name = category.getName();
        category.setNameSuggest(new Completion(new String[]{name}));
    }
}
