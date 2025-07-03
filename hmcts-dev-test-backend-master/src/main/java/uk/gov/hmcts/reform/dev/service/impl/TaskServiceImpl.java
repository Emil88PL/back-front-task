package uk.gov.hmcts.reform.dev.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskDto;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskResponseDto;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusDto;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusResponseDto;
import uk.gov.hmcts.reform.dev.dtos.UpdateTaskDto;
import uk.gov.hmcts.reform.dev.dtos.UpdateTaskResponseDto;
import uk.gov.hmcts.reform.dev.entities.Task;
import uk.gov.hmcts.reform.dev.entities.TaskStatus;
import uk.gov.hmcts.reform.dev.exceptions.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.mappers.TaskMapper;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;
import uk.gov.hmcts.reform.dev.service.TaskService;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
    public CreateTaskResponseDto getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toCreateTaskResponseDto(task);
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
    public List<CreateTaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
            .map(taskMapper::toCreateTaskResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<CreateTaskResponseDto> getAllTasksSortedByDueDateTime() {
        return taskRepository.findAll().stream()
            .sorted(Comparator.comparing(Task::getDueDateTime))
            .map(taskMapper::toCreateTaskResponseDto)
            .collect(Collectors.toList());
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
