package uk.gov.hmcts.reform.dev.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskDto;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskResponseDto;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusDto;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusResponseDto;
import uk.gov.hmcts.reform.dev.dtos.UpdateTaskDto;
import uk.gov.hmcts.reform.dev.dtos.UpdateTaskResponseDto;
import uk.gov.hmcts.reform.dev.entity.Task;
import uk.gov.hmcts.reform.dev.entity.TaskStatus;
import uk.gov.hmcts.reform.dev.exception.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.mappers.TaskMapper;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void shouldCreateTaskSuccessfully() {
        // Given
        CreateTaskDto taskDto = new CreateTaskDto();
        taskDto.setTitle("Test Task");
        taskDto.setDescription("Test Description");
        taskDto.setStatus(TaskStatus.TODO);
        taskDto.setDueDateTime(Instant.now().plus(1, java.time.temporal.ChronoUnit.DAYS));

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle(taskDto.getTitle());
        savedTask.setDescription(taskDto.getDescription());
        savedTask.setStatus(taskDto.getStatus());
        savedTask.setDueDateTime(taskDto.getDueDateTime());
        savedTask.setCreatedDate(Instant.now());

        CreateTaskResponseDto expectedResponse = new CreateTaskResponseDto();
        expectedResponse.setId(1L);
        expectedResponse.setTitle("Test Task");
        expectedResponse.setDescription("Test Description");
        expectedResponse.setStatus(TaskStatus.TODO.toString());
        expectedResponse.setDueDateTime(taskDto.getDueDateTime().toString());

        // Remove the toEntity stubbing since it's not used by the service
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        when(taskMapper.toCreateTaskResponseDto(any(Task.class))).thenReturn(expectedResponse);

        // When
        CreateTaskResponseDto result = taskService.createTask(taskDto);

        // Then
        assertNotNull(result, "Created task should not be null");
        assertEquals("Test Task", result.getTitle(), "Task title should match");
        assertEquals(1L, result.getId(), "Task ID should match");

        // Remove the toEntity verification since it's not called by the service
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toCreateTaskResponseDto(any(Task.class));
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
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Test Task");
        existingTask.setDescription("Original Description");
        existingTask.setStatus(TaskStatus.TODO);

        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        updateTaskDto.setTitle("Updated Task");

        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Original Description");
        updatedTask.setStatus(TaskStatus.TODO);

        UpdateTaskResponseDto expectedResponse = new UpdateTaskResponseDto();
        expectedResponse.setId(taskId);
        expectedResponse.setTitle("Updated Task");
        expectedResponse.setDescription("Original Description");
        expectedResponse.setStatus(TaskStatus.TODO.toString());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(updatedTask);
        when(taskMapper.toUpdateTaskResponseDto(updatedTask)).thenReturn(expectedResponse);

        // When
        UpdateTaskResponseDto result = taskService.updateTask(taskId, updateTaskDto);

        // Then
        assertNotNull(result, "Updated task should not be null");
        assertEquals("Updated Task", result.getTitle(), "Task title should be updated");
        assertEquals("Original Description", result.getDescription(), "Description should remain unchanged");
        assertEquals(TaskStatus.TODO.toString(), result.getStatus(), "Status should remain unchanged");
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTask);
        verify(taskMapper).toUpdateTaskResponseDto(updatedTask);
    }

    @Test
    void shouldUpdateOnlyTitleWhenOtherFieldsAreNull() {
        // Given
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setStatus(TaskStatus.TODO);

        UpdateTaskDto updateDto = new UpdateTaskDto();
        updateDto.setTitle("New Title");

        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("New Title");
        updatedTask.setDescription("Old Description");
        updatedTask.setStatus(TaskStatus.TODO);

        UpdateTaskResponseDto expectedResponse = new UpdateTaskResponseDto();
        expectedResponse.setId(taskId);
        expectedResponse.setTitle("New Title");
        expectedResponse.setDescription("Old Description");
        expectedResponse.setStatus(TaskStatus.TODO.toString());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(updatedTask);
        when(taskMapper.toUpdateTaskResponseDto(updatedTask)).thenReturn(expectedResponse);

        // When
        UpdateTaskResponseDto result = taskService.updateTask(taskId, updateDto);

        // Then
        assertNotNull(result, "Updated task should not be null");
        assertEquals("New Title", result.getTitle(), "Task title should be updated");
        assertEquals("Old Description", result.getDescription(), "Description should remain unchanged");
        assertEquals(TaskStatus.TODO.toString(), result.getStatus(), "Status should remain unchanged");
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTask);
        verify(taskMapper).toUpdateTaskResponseDto(updatedTask);
    }

    @Test
    void shouldUpdateTaskTitleDescriptionAndStatus() {
        // Given
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setStatus(TaskStatus.TODO);

        UpdateTaskDto updateDto = new UpdateTaskDto();
        updateDto.setTitle("New Title");
        updateDto.setDescription("New Description");
        updateDto.setStatus(TaskStatus.DONE);

        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("New Title");
        updatedTask.setDescription("New Description");
        updatedTask.setStatus(TaskStatus.DONE);

        UpdateTaskResponseDto expectedResponse = new UpdateTaskResponseDto();
        expectedResponse.setId(taskId);
        expectedResponse.setTitle("New Title");
        expectedResponse.setDescription("New Description");
        expectedResponse.setStatus(TaskStatus.DONE.toString());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(updatedTask);
        when(taskMapper.toUpdateTaskResponseDto(updatedTask)).thenReturn(expectedResponse);

        // When
        UpdateTaskResponseDto result = taskService.updateTask(taskId, updateDto);

        // Then
        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals(TaskStatus.DONE.toString(), result.getStatus());
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTask);
        verify(taskMapper).toUpdateTaskResponseDto(updatedTask);
    }

    @Test
    void shouldUpdateTaskStatusSuccessfully() {
        // Given
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setStatus(TaskStatus.TODO);

        TaskStatusDto statusDto = new TaskStatusDto();
        statusDto.setStatus(TaskStatus.IN_PROGRESS.name());

        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setStatus(TaskStatus.IN_PROGRESS);

        TaskStatusResponseDto expectedResponse = new TaskStatusResponseDto();

        expectedResponse.setStatus(TaskStatus.IN_PROGRESS.toString());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(updatedTask);
        when(taskMapper.toTaskStatusResponseDto(updatedTask)).thenReturn(expectedResponse);

        // When
        TaskStatusResponseDto result = taskService.updateTaskStatus(taskId, statusDto);

        // Then
        assertNotNull(result, "Updated task should not be null");
        assertEquals(TaskStatus.IN_PROGRESS.toString(), result.getStatus(), "Status should be updated");
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTask);
        verify(taskMapper).toTaskStatusResponseDto(updatedTask);
    }

    @Test
    void shouldThrowTaskNotFoundExceptionWhenUpdatingStatus() {
        // Given
        Long taskId = 99L;

        TaskStatusDto statusDto = new TaskStatusDto();
        statusDto.setStatus(TaskStatus.DONE.toString());

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskStatus(taskId, statusDto));

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toTaskStatusResponseDto(any(Task.class));
    }

    @Test
    void shouldReturnAllTasks() {
        // Given
        List<Task> tasks = List.of(new Task(), new Task());
        when(taskRepository.findAll()).thenReturn(tasks);

        // When
        Iterable<Task> result = taskService.getAllTasks();

        // Then
        assertNotNull(result);
        assertIterableEquals(tasks, result);
        verify(taskRepository).findAll();
    }

    @Test
    void shouldReturnAllTasksSortedByDueDateTime() {
        // Given
        List<Task> sortedTasks = List.of(new Task(), new Task());
        when(taskRepository.findAllByOrderByDueDateTimeAsc()).thenReturn(sortedTasks);

        // When
        List<Task> result = taskService.getAllTasksSortedByDueDateTime();

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
