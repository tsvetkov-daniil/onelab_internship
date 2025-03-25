package tsvetkov.daniil.util.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    private final ObjectMapper objectMapper;
    public Mapper() {
        objectMapper = new ObjectMapper();
    }

    public <S, T> T map(S source, Class<T> targetClass) {
        return objectMapper.convertValue(source, targetClass);
    }
}

