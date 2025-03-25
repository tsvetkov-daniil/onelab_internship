package tsvetkov.daniil.book.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tsvetkov.daniil.book.entity.Category;
import tsvetkov.daniil.dto.CategoryDTO;
import tsvetkov.daniil.util.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class CategoryEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Mapper mapper;

    public void addCategoryToSearch(Category category) {
        CategoryDTO categoryDTO = mapper.map(category, CategoryDTO.class);
        kafkaTemplate.send("category-create-search", categoryDTO);
    }

    public void deleteCategoryFromSearch(Long categoryId) {
        kafkaTemplate.send("category-delete-search", categoryId);
    }

}
