package uk.gov.hmcts.reform.dev.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.dev.entity.TaskEntity;
import uk.gov.hmcts.reform.dev.entity.TaskStatus;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;
import uk.gov.hmcts.reform.dev.service.TaskService;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

//    @Bean
//    CommandLineRunner initDatabase(TaskRepository taskRepository) {
//        return args -> {
//            taskRepository.save(new TaskEntity("Task 1", "Description 1", TaskStatus.TODO, LocalDateTime.now()));
//            taskRepository.save(new TaskEntity("Task 2", "Description 2", TaskStatus.TODO, LocalDateTime.now()));
//            taskRepository.save(new TaskEntity("Task 3", "Description 3", TaskStatus.TODO, LocalDateTime.now()));
//        };
//    }
}
