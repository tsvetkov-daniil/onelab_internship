package tsvetkov.daniil.book;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

@EnableProcessApplication
@SpringBootApplication
@EnableDiscoveryClient
public class MainBook {
    public static void main(String[] args) {
       ApplicationContext ctx =  SpringApplication.run(MainBook.class, args);
       ProcessEngine processEngine = ctx.getBean(ProcessEngine.class);
//        List<ProcessDefinition> processDefinitions =

        System.out.println("_____________________________________________________________________________________________________");
        System.err.println(processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .list());
    }
    
}