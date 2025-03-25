package tsvetkov.daniil;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;


public class RepositoryLoggingAspect {

    @Pointcut("execution(* tsvetkov.daniil.review.repository.*.*(..))")
    public void repositoryMethods() {}

    @Pointcut("execution(* tsvetkov.daniil.review.repository.BookRepository.findAuthorsByBookId(..))")
    public void repositoryMethodsException() {}

    @Pointcut("execution(* tsvetkov.daniil.review.repository.*.*(..)) && args(..)")
    public void methodWithArgs() {}

    @Before("methodWithArgs()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("[BEFORE] Вызов метода: " + joinPoint.getSignature().getName() +
                " с аргументами: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "repositoryMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("[AFTER RETURNING] Метод " + joinPoint.getSignature().getName() +
                " успешно выполнен. Возвращаемое значение: " + result);
    }

    @AfterThrowing(pointcut = "repositoryMethodsException()", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        System.err.println("[AFTER THROWING] Ошибка в методе " + joinPoint.getSignature().getName() +
                ". Ошибка: " + error.getMessage());
    }

    @After("repositoryMethods()")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("[AFTER] Метод " + joinPoint.getSignature().getName() + " завершил выполнение.");
    }

    @Around("repositoryMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        System.out.println("[AROUND] Выполнение метода: " + joinPoint.getSignature().getName());

        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("[AROUND] Метод " + joinPoint.getSignature().getName() +
                    " выполнен за " + elapsedTime + " мс.");
            return result;
        } catch (Throwable e) {
            System.err.println("[AROUND] Ошибка в методе " + joinPoint.getSignature().getName() +
                    ": " + e.getMessage());
            throw e;
        }
    }
}


