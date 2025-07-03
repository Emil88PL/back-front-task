package uk.gov.hmcts.reform.dev.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import uk.gov.hmcts.reform.dev.entities.TaskStatus;
import java.time.Instant;

public class CreateTaskDto {

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 50, message = "Title cannot exceed 50 characters")
    private String title;

    @Size(max = 200, message = "Description cannot exceed 200 characters")
    private String description;

    @NotNull(message = "Tasks status is required")
    private TaskStatus status;

    @FutureOrPresent(message = "Due date must be in the future")
    @NotNull(message = "Due date is mandatory")
    private Instant dueDateTime;

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

    public Instant getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(Instant dueDateTime) {
        this.dueDateTime = dueDateTime;
    }
}
