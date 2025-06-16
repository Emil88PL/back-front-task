package uk.gov.hmcts.reform.dev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
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

@Service
@Validated
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public CreateTaskResponseDto createTask(CreateTaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setDueDateTime(taskDto.getDueDateTime());
        task.setCreatedDate(Instant.now());
        taskRepository.save(task);
        return taskMapper.toCreateTaskResponseDto(task);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Override
    public UpdateTaskResponseDto updateTask(Long id, UpdateTaskDto updateTaskDto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

        if (updateTaskDto.getTitle() != null) {
            task.setTitle(updateTaskDto.getTitle());
        }

        if (updateTaskDto.getDescription() != null) {
            task.setDescription(updateTaskDto.getDescription());
        }

        if (updateTaskDto.getStatus() != null) {
            task.setStatus(updateTaskDto.getStatus());
        }

        if (updateTaskDto.getDueDateTime() != null) {
            task.setDueDateTime(updateTaskDto.getDueDateTime());
        }

        Task savedTask = taskRepository.save(task);
        return taskMapper.toUpdateTaskResponseDto(savedTask);
    }

    @Override
    public Iterable<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getAllTasksSortedByDueDateTime() {
        return taskRepository.findAllByOrderByDueDateTimeAsc();
    }

    @Override
    public TaskStatusResponseDto updateTaskStatus(Long id, TaskStatusDto newStatusDto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        TaskStatus newStatus = newStatusDto.toEnum();
        task.setStatus(newStatus);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toTaskStatusResponseDto(savedTask);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }
}
