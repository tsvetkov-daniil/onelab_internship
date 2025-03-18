package tsvetkov.daniil;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class Validation {
    @Pointcut("execution(* tsvetkov.daniil.review.service.*.*(..))")
    public void serviceMethods() {
    }

    @Around("serviceMethods()")
    public Object checkArguments(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg == null) {
                log.warn("Метод {} не будет выполнен, так как один из аргументов равен null", joinPoint.getSignature());
                return null;
            }
        }
        return joinPoint.proceed();
    }

}

