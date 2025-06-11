package uk.gov.hmcts.reform.dev.dtos;

import uk.gov.hmcts.reform.dev.entity.TaskStatus;
import java.time.LocalDateTime;

public class UpdateTaskDto {

    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime dueDateTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }
}
