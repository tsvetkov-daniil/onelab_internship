package tsvetkov.daniil.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class RestControllerLoggingAspect {

    private final ObjectMapper objectMapper;

    public RestControllerLoggingAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {
    }

    @Around("restControllerMethods()")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        Object[] args = joinPoint.getArgs();
        String requestBody = args.length > 0 ? objectMapper.writeValueAsString(args) : "No body";

        log.info("Request: {} {} {} | Body: {}", method, uri, queryString != null ? "?" + queryString : "", requestBody);

        Object result = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - startTime;
        String responseBody = result instanceof ResponseEntity ?
                objectMapper.writeValueAsString(((ResponseEntity<?>) result).getBody()) : objectMapper.writeValueAsString(result);

        log.info("Response: {} {} | Status: {} | Body: {} | Time: {}ms",
                method, uri, result instanceof ResponseEntity ? ((ResponseEntity<?>) result).getStatusCode() : "N/A",
                responseBody, executionTime);

        return result;
    }
}
