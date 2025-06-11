package uk.gov.hmcts.reform.dev.service;

import uk.gov.hmcts.reform.dev.dtos.UpdateTaskDto;
import uk.gov.hmcts.reform.dev.entity.TaskEntity;
import uk.gov.hmcts.reform.dev.entity.TaskStatus;

import java.util.List;

public interface TaskService {

    TaskEntity getTaskById(Long id);
    List<TaskEntity> getAllTasksSortedByDueDateTime();

    Iterable<TaskEntity> getAllTasks();

    TaskEntity updateTaskStatus(Long id, TaskStatus newStatus);

    void deleteTask(Long id);

    TaskEntity createTask(TaskEntity task);

    TaskEntity updateTask(Long id, UpdateTaskDto updateTaskDto);

}
