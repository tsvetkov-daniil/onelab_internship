package tsvetkov.daniil.search.dto;

public record BookSearchRequest(
        Double minPrice,
        Double maxPrice,
        Long categoryId,
        Long authorId,
        String query,
        int page,
        int size
) {}
