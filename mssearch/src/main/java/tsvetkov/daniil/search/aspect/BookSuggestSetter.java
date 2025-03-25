package tsvetkov.daniil.search.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import tsvetkov.daniil.search.entity.Book;
import tsvetkov.daniil.search.util.EnhancedCompletion;

@Aspect
public class BookSuggestSetter {

    @Pointcut("execution(* tsvetkov.daniil.search.dto.Book$BookBuilder.title(String))")
    public void bookTitleViaBuilder() {
    }


    @Pointcut("execution(* tsvetkov.daniil.search.dto.Book.setTitle(String))")
    public void bookTitleViaSetter() {
    }

    @Around("bookTitleViaBuilder() || bookTitleViaSetter()")
    public void setBookTitleSuggest(ProceedingJoinPoint joinPoint) throws Throwable {
        final Object obj = joinPoint.proceed();
        if (obj instanceof Book book)
            bookTitleSuggestSetter(book);
        else if (joinPoint.getTarget() instanceof Book book)
            bookTitleSuggestSetter(book);
    }

    private void bookTitleSuggestSetter(Book book) {
        final String title = book.getTitle();
        book.setTitle(title);
        book.setTitleSuggest(new EnhancedCompletion(new String[]{title}));
    }
}
