package uk.gov.hmcts.reform.dev.dtos;

import jakarta.validation.constraints.NotBlank;
import uk.gov.hmcts.reform.dev.validation.ValueOfEnum;
import uk.gov.hmcts.reform.dev.entities.TaskStatus;

public class TaskStatusDto {

    @NotBlank(message = "Status is required")
    @ValueOfEnum(enumClass = TaskStatus.class, message = "Status must be one of: To do, In progress, Done")
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

