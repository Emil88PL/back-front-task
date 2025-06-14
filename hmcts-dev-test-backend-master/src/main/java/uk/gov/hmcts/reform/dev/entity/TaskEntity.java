package uk.gov.hmcts.reform.dev.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusDto;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title cannot be empty")
    @Size(max = 50, message = "Title cannot exceed 50 characters")
    private String title;

    @Size(max = 200, message = "Description cannot exceed 200 characters")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Tasks status is required")
    private TaskStatus status;

    @FutureOrPresent(message = "Due date must be today or in the future")
    @NotNull(message = "Due date is mandatory")
    private LocalDateTime dueDateTime;

    @NotNull(message = "Created date is mandatory")
    private LocalDateTime createdDate;

    public TaskEntity() {
    }

    public TaskEntity(String title,
                      String description,
                      TaskStatus status,
                      LocalDateTime dueDateTime,
                      LocalDateTime createdDate) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDateTime = dueDateTime;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
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

    public void setStatus(@NotNull(message = "Tasks status is required") TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(
            description,
            that.description
        ) && status == that.status && Objects.equals(dueDateTime, that.dueDateTime) && Objects.equals(
            createdDate,
            that.createdDate
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, dueDateTime, createdDate);
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", status=" + status +
            ", dueDateTime=" + dueDateTime +
            ", createdDate=" + createdDate +
            '}';
    }
}
