package tsvetkov.daniil.auth.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tsvetkov.daniil.auth.dto.Reader;
import tsvetkov.daniil.auth.service.ReaderService;

import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"reader_to_author"})
@EnableKafka
class EventConsumerTest {

    @Autowired
    private KafkaTemplate<String, Reader> kafkaTemplate;

    @Autowired
    private EventConsumer eventConsumer;

    @MockitoBean
    private ReaderService readerService;

    @MockitoBean
    private EventProducer eventProducer;

    private Reader reader;

    @BeforeEach
    void setUp() {
        reader = Reader.builder()
                .email("sadfdsfds@gmail.com")
                .aboutMe("Its me")
                .firstName("Nick")
                .lastName("Milishevich")
                .familyName("Pit")
                .nickname("kerat")
                .build();
    }

    @Test
    void testListener() {
        kafkaTemplate.send("reader_to_author", reader);

        verify(eventProducer, Mockito.times(1)).addAuthorToSearch(reader);
    }
}
