package uk.gov.hmcts.reform.dev.domain.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @Column(name = "due_date")
    private Instant dueDateTime;

    @Column(name = "created_task_date")
    private Instant createdDate;

    public Task() {
    }

    public Task(String title,
                String description,
                TaskStatus status,
                Instant dueDateTime,
                Instant createdDate) {
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

    public Instant getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(Instant dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

}
