package tsvetkov.daniil.worker.service;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class BookApprovalWorker {


    @ExternalTaskSubscription(topicName = "approve-book-topic")
    public void handleApproval(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String bookId = externalTask.getVariable("bookId");

        // Логика для одобрения книги
        boolean isApproved = approveBook(bookId);

        // Завершаем задачу в Camunda
        externalTaskService.complete(externalTask, Collections.singletonMap("isApproved", isApproved));
    }

    private boolean approveBook(String bookId) {
        // Логика для одобрения книги
        // Например, обновление статуса книги в базе данных
        return true; // или false в зависимости от логики
    }
}
