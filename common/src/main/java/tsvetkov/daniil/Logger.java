package tsvetkov.daniil;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

@Slf4j
@Aspect
public class Logger {
    @Pointcut("execution(* tsvetkov.daniil.*.*.*(..))")
    public void allMethods() {
    }

    @Pointcut("execution(* tsvetkov.daniil.review.service.*.*(..))")
    public void serviceMethods() {
    }

    @Pointcut("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public void listenerMethods() {
    }

    @Pointcut("execution(* tsvetkov.daniil.event.*.*(..)) && !@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public void producerMethods() {
    }

    @Pointcut("execution(* tsvetkov.daniil.review.repository.*.(..))")
    public void repositoryMethods() {
    }



    @Before("listenerMethods()")
    public void logKafkaListenerBefore(JoinPoint joinPoint) {
        log.info("Слушатель " + joinPoint.getSignature().toLongString() + " получил объект " + Arrays.deepToString(joinPoint.getArgs()));
    }

    @AfterReturning("listenerMethods()")
    public void logKafkaListenerAfter(JoinPoint joinPoint) {
        log.info("Слушатель " + joinPoint.getSignature().toLongString() + " завершил работу");
    }

    @Before("producerMethods()")
    public void logKafkaProducerBefore(JoinPoint joinPoint) {
        log.info("Слушатель " + joinPoint.getSignature().toLongString() + " получил объект " + Arrays.deepToString(joinPoint.getArgs()));
    }

    @AfterReturning("producerMethods()")
    public void logKafkaProducerAfter(JoinPoint joinPoint) {
        log.info("Слушатель " + joinPoint.getSignature().toLongString() + " завершил работу");
    }

    @Before("serviceMethods()")
    public void logServiceBefore(JoinPoint joinPoint) {
        log.info("Запуск сервисного метода: } " + joinPoint.getSignature().toLongString());

    }

    @AfterReturning("serviceMethods()")
    public void logServiceAfter(JoinPoint joinPoint) {
        log.info("Метод: " + joinPoint.getSignature().toLongString() + " отработал");
    }

    @Before("repositoryMethods()")
    public void logRepositoryBefore(JoinPoint joinPoint) {
        log.info("Запуск из репозитория метода: } " + joinPoint.getSignature().toLongString());

    }

    @AfterReturning("serviceMethods()")
    public void logRepositoryAfter(JoinPoint joinPoint) {
        log.info("Метод: " + joinPoint.getSignature().toLongString() + " из репозитория отработал");
    }

    @AfterThrowing(value = "allMethods()", throwing = "exception")
    public void logErrors(JoinPoint joinPoint, Throwable exception) {
        log.error("Метод " + joinPoint.getSignature().toLongString() + " выбросил исключение: " + exception.getMessage(), exception);
    }
}
