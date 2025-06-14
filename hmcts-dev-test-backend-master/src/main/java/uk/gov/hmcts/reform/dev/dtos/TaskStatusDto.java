package uk.gov.hmcts.reform.dev.dtos;

import uk.gov.hmcts.reform.dev.entity.TaskStatus;

public class TaskStatusDto {

    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
