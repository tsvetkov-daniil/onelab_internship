package tsvetkov.daniil.book.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleAuthorNotFoundException(AuthorNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBookNotFoundException(BookNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(BookStatusNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBookStatusNotFoundException(BookStatusNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCommentNotFoundException(CommentNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidBookOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidBookOperationException(InvalidBookOperationException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(LanguageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleLanguageNotFoundException(LanguageNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ScoreNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleScoreNotFoundException(ScoreNotFoundException ex) {
        return ex.getMessage();
    }
}
