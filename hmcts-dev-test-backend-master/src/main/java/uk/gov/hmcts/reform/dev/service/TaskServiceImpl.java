package uk.gov.hmcts.reform.dev.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.dtos.UpdateTaskDto;
import uk.gov.hmcts.reform.dev.entity.TaskEntity;
import uk.gov.hmcts.reform.dev.entity.TaskStatus;
import uk.gov.hmcts.reform.dev.exception.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskEntity createTask(TaskEntity task) {
        if(task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }
        return taskRepository.save(task);
    }

    @Override
    public TaskEntity getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Override
    public TaskEntity updateTask(Long id, UpdateTaskDto updateTaskDto) {
        TaskEntity task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

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

        return taskRepository.save(task);
    }

    @Override
    public Iterable<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<TaskEntity> getAllTasksSortedByDueDateTime() {
        return taskRepository.findAllByOrderByDueDateTimeDesc();
    }

    @Override
    public TaskEntity updateTaskStatus(Long id, TaskStatus newStatus) {
        TaskEntity task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
