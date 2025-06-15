package uk.gov.hmcts.reform.dev.dtos;

import jakarta.validation.constraints.NotBlank;
import uk.gov.hmcts.reform.dev.configuration.ValueOfEnum;
import uk.gov.hmcts.reform.dev.entity.TaskStatus;

public class TaskStatusDto {

    @NotBlank(message = "Status is required")
    @ValueOfEnum(enumClass = TaskStatus.class, message = "Status must be one of: TODO, IN_PROGRESS, DONE")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TaskStatus toEnum() {
        return TaskStatus.valueOf(status.toUpperCase());
    }
}

