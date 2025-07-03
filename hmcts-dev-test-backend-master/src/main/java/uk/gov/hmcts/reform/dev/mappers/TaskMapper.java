package uk.gov.hmcts.reform.dev.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskDto;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskResponseDto;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusDto;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusResponseDto;
import uk.gov.hmcts.reform.dev.dtos.UpdateTaskResponseDto;
import uk.gov.hmcts.reform.dev.entities.Task;
import uk.gov.hmcts.reform.dev.entities.TaskStatus;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    Task toEntity(CreateTaskDto dto);

    CreateTaskResponseDto toCreateTaskResponseDto(Task task);

    TaskStatusResponseDto toTaskStatusResponseDto(Task task);

    UpdateTaskResponseDto toUpdateTaskResponseDto(Task task);

    TaskStatusDto toDto(TaskStatus status);

}
