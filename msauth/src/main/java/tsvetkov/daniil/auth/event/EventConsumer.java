package tsvetkov.daniil.auth.event;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tsvetkov.daniil.auth.entity.Reader;
import tsvetkov.daniil.auth.service.ReaderService;



@Service
public class EventConsumer {

    private final ReaderService readerService;
    private final EventProducer eventProducer;

    @Autowired
    public EventConsumer(ReaderService readerService, EventProducer eventProducer) {
        this.readerService = readerService;
        this.eventProducer = eventProducer;
    }

    @KafkaListener(topics = "reader_to_author", groupId = "reader-group")
    public void listener(Reader reader){
        eventProducer.addAuthorToSearch(reader);
    }
}
