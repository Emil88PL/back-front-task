package uk.gov.hmcts.reform.dev.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.dev.entity.TaskEntity;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void shouldCreateTaskSuccessfully() {

        TaskEntity task = new TaskEntity();
        task.setTitle("Test Task");
        when(taskRepository.save(task)).thenReturn(task);

        TaskEntity result = taskService.createTask(task);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository).save(task);
    }
}
