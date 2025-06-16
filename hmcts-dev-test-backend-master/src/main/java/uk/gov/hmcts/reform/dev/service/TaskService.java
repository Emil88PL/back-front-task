package uk.gov.hmcts.reform.dev.service;

import uk.gov.hmcts.reform.dev.dtos.CreateTaskDto;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskResponseDto;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusDto;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusResponseDto;
import uk.gov.hmcts.reform.dev.dtos.UpdateTaskDto;
import uk.gov.hmcts.reform.dev.dtos.UpdateTaskResponseDto;
import java.util.List;

public interface TaskService {

    CreateTaskResponseDto createTask(CreateTaskDto taskDto);

    CreateTaskResponseDto getTaskById(Long id);

    UpdateTaskResponseDto updateTask(Long id, UpdateTaskDto updateTaskDto);

    List<CreateTaskResponseDto> getAllTasks();

    List<CreateTaskResponseDto> getAllTasksSortedByDueDateTime();

    TaskStatusResponseDto updateTaskStatus(Long id, TaskStatusDto newStatusDto);

    void deleteTask(Long id);

    void deleteAllTasks();
}
