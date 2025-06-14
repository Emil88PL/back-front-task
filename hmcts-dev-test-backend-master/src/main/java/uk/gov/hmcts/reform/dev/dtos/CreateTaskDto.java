package uk.gov.hmcts.reform.dev.dtos;

import jakarta.validation.constraints.*;
import uk.gov.hmcts.reform.dev.entity.TaskStatus;

import java.time.LocalDateTime;

public class CreateTaskDto {

    @NotNull(message = "Title cannot be empty")
    @Size(max = 50, message = "Title cannot exceed 50 characters")
    private String title;

    @Size(max = 200, message = "Description cannot exceed 200 characters")
    private String description;

    @NotNull(message = "Tasks status is required")
    private TaskStatus status;

    @FutureOrPresent(message = "Due date must be today or in the future")
    @NotNull(message = "Due date is mandatory")
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

    public @NotNull(message = "Tasks status is required") TaskStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "Tasks status is required") TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }
}
