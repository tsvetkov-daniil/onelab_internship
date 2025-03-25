package tsvetkov.daniil.search.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import tsvetkov.daniil.search.entity.Author;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorSetDeserializer extends JsonDeserializer<Set<Author>> {

    @Override
    public Set<Author> deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        Set<Long> authorIds = p.readValueAs(new TypeReference<Set<Long>>() {});
        return authorIds.stream()
                .map(id -> Author.builder()
                        .id(id)
                        .build())
                .collect(Collectors.toSet());
    }
}
