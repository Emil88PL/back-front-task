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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");

        List<Task> tasks = List.of(task1, task2);

        CreateTaskResponseDto responseDto1 = new CreateTaskResponseDto();
        responseDto1.setId(1L);
        responseDto1.setTitle("Task 1");

        CreateTaskResponseDto responseDto2 = new CreateTaskResponseDto();
        responseDto2.setId(2L);
        responseDto2.setTitle("Task 2");

        List<CreateTaskResponseDto> expectedResponse = List.of(responseDto1, responseDto2);

        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.toCreateTaskResponseDto(task1)).thenReturn(responseDto1);
        when(taskMapper.toCreateTaskResponseDto(task2)).thenReturn(responseDto2);

        // When
        List<CreateTaskResponseDto> result = taskService.getAllTasks();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertIterableEquals(expectedResponse, result);
        verify(taskRepository).findAll();
        verify(taskMapper).toCreateTaskResponseDto(task1);
        verify(taskMapper).toCreateTaskResponseDto(task2);
    }

    @Test
    void shouldReturnAllTasksSortedByDueDateTime() {
        // Given
        Instant now = Instant.now();

        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDueDateTime(now);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDueDateTime(now.plus(1, ChronoUnit.DAYS));

        List<Task> unsorted = List.of(task2, task1); // out‑of‑order on purpose

        CreateTaskResponseDto dto1 = new CreateTaskResponseDto();
        dto1.setId(1L);
        dto1.setTitle("Task 1");
        dto1.setDueDateTime(now.toString());

        CreateTaskResponseDto dto2 = new CreateTaskResponseDto();
        dto2.setId(2L);
        dto2.setTitle("Task 2");
        dto2.setDueDateTime(now.plus(1, ChronoUnit.DAYS).toString());

        List<CreateTaskResponseDto> expected = List.of(dto1, dto2);

        // Mock repository & mapper
        when(taskRepository.findAll()).thenReturn(unsorted);
        when(taskMapper.toCreateTaskResponseDto(task1)).thenReturn(dto1);
        when(taskMapper.toCreateTaskResponseDto(task2)).thenReturn(dto2);

        // When
        List<CreateTaskResponseDto> actual = taskService.getAllTasksSortedByDueDateTime();

        // Then
        assertNotNull(actual);
        assertEquals(expected, actual);

        // Verify correct repo & mapper usage
        verify(taskRepository).findAll();
        verify(taskMapper).toCreateTaskResponseDto(task1);
        verify(taskMapper).toCreateTaskResponseDto(task2);
        verifyNoMoreInteractions(taskRepository, taskMapper);
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
