package uk.gov.hmcts.reform.dev.service;

import uk.gov.hmcts.reform.dev.dtos.UpdateTaskDto;
import uk.gov.hmcts.reform.dev.entity.TaskEntity;
import uk.gov.hmcts.reform.dev.entity.TaskStatus;

import java.util.List;

public interface TaskService {

    TaskEntity createTask(TaskEntity task);

    TaskEntity getTaskById(Long id);

    TaskEntity updateTask(Long id, UpdateTaskDto updateTaskDto);

    Iterable<TaskEntity> getAllTasks();

    List<TaskEntity> getAllTasksSortedByDueDateTime();

    TaskEntity updateTaskStatus(Long id, TaskStatus newStatus);

    void deleteTask(Long id);

}
