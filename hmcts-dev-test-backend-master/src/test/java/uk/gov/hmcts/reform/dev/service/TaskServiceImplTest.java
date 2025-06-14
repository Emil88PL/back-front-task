package uk.gov.hmcts.reform.dev.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskDto;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusDto;
import uk.gov.hmcts.reform.dev.dtos.UpdateTaskDto;
import uk.gov.hmcts.reform.dev.entity.TaskEntity;
import uk.gov.hmcts.reform.dev.entity.TaskStatus;
import uk.gov.hmcts.reform.dev.exception.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void shouldCreateTaskSuccessfully() {
        // Given
        CreateTaskDto taskDto = new CreateTaskDto();
        taskDto.setTitle("Test Task");
        taskDto.setDescription("Test Description");
        taskDto.setStatus(TaskStatus.TODO);
        // add one day to a Instant date
        taskDto.setDueDateTime(Instant.now().plus(1, java.time.temporal.ChronoUnit.DAYS));

        TaskEntity savedTask = new TaskEntity();
        savedTask.setId(1L); // Simulate the DB generating an ID
        savedTask.setTitle(taskDto.getTitle());
        savedTask.setDescription(taskDto.getDescription());
        savedTask.setStatus(taskDto.getStatus());
        savedTask.setDueDateTime(taskDto.getDueDateTime());
        savedTask.setCreatedDate(Instant.now());

        when(taskRepository.save(any(TaskEntity.class))).thenReturn(savedTask);

        // When
        TaskEntity result = taskService.createTask(taskDto);

        // Then
        assertNotNull(result, "Created task should not be null");
        assertEquals(savedTask.getId(), result.getId(), "Task ID should be set by the save operation");
        assertEquals("Test Task", result.getTitle(), "Task title should match");
        verify(taskRepository).save(any(TaskEntity.class)); // Verify that the save method was called
    }

    @Test
    void shouldThrowTaskNotFoundExceptionWhenTaskDoesNotExist() {
        // Given
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class,
                                                       () -> taskService.getTaskById(taskId),
                                                       "Should throw TaskNotFoundException for non-existent task");
        assertEquals("Task not found with id: " + taskId, exception.getMessage(),
                     "Exception message should match");
        verify(taskRepository).findById(taskId);
    }

    @Test
    void shouldUpdateTaskSuccessfully() {
        // Given
        TaskEntity existingTask = new TaskEntity();
        existingTask.setId(1L);
        existingTask.setTitle("Test Task");
        existingTask.setDescription("Original Description");
        existingTask.setStatus(TaskStatus.TODO);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(existingTask);

        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        updateTaskDto.setTitle("Updated Task");

        // When
        TaskEntity result = taskService.updateTask(1L, updateTaskDto);

        // Then
        assertNotNull(result, "Updated task should not be null");
        assertEquals("Updated Task", result.getTitle(), "Task title should be updated");
        assertEquals("Original Description", result.getDescription(), "Description should remain unchanged");
        assertEquals(TaskStatus.TODO, result.getStatus(), "Status should remain unchanged");
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(existingTask);
    }

    @Test
    void shouldUpdateOnlyTitleWhenOtherFieldsAreNull() {
        // Given
        Long taskId = 1L;
        TaskEntity existingTask = new TaskEntity();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setStatus(TaskStatus.TODO);

        UpdateTaskDto updateDto = new UpdateTaskDto();
        updateDto.setTitle("New Title");
        // description and status are null

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenAnswer(invocation -> {
            TaskEntity task = invocation.getArgument(0);
            task.setTitle("New Title");
            return task;
        });

        // When
        TaskEntity result = taskService.updateTask(taskId, updateDto);

        // Then
        assertNotNull(result, "Updated task should not be null");
        assertEquals("New Title", result.getTitle(), "Task title should be updated");
        assertEquals("Old Description", result.getDescription(), "Description should remain unchanged");
        assertEquals(TaskStatus.TODO, result.getStatus(), "Status should remain unchanged");
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTask);
    }

    @Test
    void shouldUpdateTaskTitleDescriptionAndStatus() {
        // Given
        Long taskId = 1L;
        TaskEntity existingTask = new TaskEntity();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setStatus(TaskStatus.TODO);

        UpdateTaskDto updateDto = new UpdateTaskDto();
        updateDto.setTitle("New Title");
        updateDto.setDescription("New Description");
        updateDto.setStatus(TaskStatus.DONE);

        TaskEntity updatedTask = new TaskEntity();
        updatedTask.setId(taskId);
        updatedTask.setTitle("New Title");
        updatedTask.setDescription("New Description");
        updatedTask.setStatus(TaskStatus.DONE);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(updatedTask);

        // When
        TaskEntity result = taskService.updateTask(taskId, updateDto);

        // Then
        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals(TaskStatus.DONE, result.getStatus());
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTask);
    }

    @Test
    void shouldUpdateTaskStatusSuccessfully() {
        // Given
        Long taskId = 1L;
        TaskEntity existingTask = new TaskEntity();
        existingTask.setId(taskId);
        existingTask.setStatus(TaskStatus.TODO);

        TaskStatusDto statusDto = new TaskStatusDto();
        statusDto.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        // When
        TaskEntity result = taskService.updateTaskStatus(taskId, statusDto);

        // Then
        assertNotNull(result, "Updated task should not be null");
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus(), "Status should be updated");
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTask);
    }

    @Test
    void shouldThrowTaskNotFoundExceptionWhenUpdatingStatus() {
        // Given
        Long taskId = 99L;

        TaskStatusDto statusDto = new TaskStatusDto();
        statusDto.setStatus(TaskStatus.DONE);

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskStatus(taskId, statusDto));

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    void shouldReturnAllTasks() {
        // Given
        List<TaskEntity> tasks = List.of(new TaskEntity(), new TaskEntity());
        when(taskRepository.findAll()).thenReturn(tasks);

        // When
        Iterable<TaskEntity> result = taskService.getAllTasks();

        // Then
        assertNotNull(result);
        assertIterableEquals(tasks, result);
        verify(taskRepository).findAll();
    }

    @Test
    void shouldReturnAllTasksSortedByDueDateTime() {
        // Given
        List<TaskEntity> sortedTasks = List.of(new TaskEntity(), new TaskEntity());
        when(taskRepository.findAllByOrderByDueDateTimeAsc()).thenReturn(sortedTasks);

        // When
        List<TaskEntity> result = taskService.getAllTasksSortedByDueDateTime();

        // Then
        assertNotNull(result);
        assertEquals(sortedTasks, result);
        verify(taskRepository).findAllByOrderByDueDateTimeAsc();
    }

    @Test
    void shouldDeleteTaskSuccessfully() {
        // Given
        Long taskId = 1L;

        // When
        taskService.deleteTask(taskId);

        // Then
        verify(taskRepository).deleteById(taskId);
    }

}
