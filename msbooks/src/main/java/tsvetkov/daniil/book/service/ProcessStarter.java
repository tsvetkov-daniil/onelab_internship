package tsvetkov.daniil.book.service;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ProcessStarter implements ApplicationRunner {

    private final RuntimeService runtimeService;

    public ProcessStarter(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
      //  runtimeService.startProcessInstanceByKey("r");
    }
}
