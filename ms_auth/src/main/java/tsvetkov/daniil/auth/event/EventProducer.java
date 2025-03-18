package tsvetkov.daniil.auth.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tsvetkov.daniil.auth.dto.Reader;

@Service
public class EventProducer {

    private final KafkaTemplate<String, Reader> readerProducer;

    @Autowired
    public EventProducer(KafkaTemplate<String, Reader> readerProducer) {
        this.readerProducer = readerProducer;
    }

    public void addAuthorToSearch(Reader reader)
    {
        readerProducer.send("add_author_search", reader);
    }
}
