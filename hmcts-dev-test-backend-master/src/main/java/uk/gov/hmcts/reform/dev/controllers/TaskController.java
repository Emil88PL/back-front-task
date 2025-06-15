package uk.gov.hmcts.reform.dev.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.dtos.CreateTaskDto;
import uk.gov.hmcts.reform.dev.dtos.TaskStatusDto;
import uk.gov.hmcts.reform.dev.dtos.UpdateTaskDto;
import uk.gov.hmcts.reform.dev.entity.TaskEntity;
import uk.gov.hmcts.reform.dev.entity.TaskStatus;
import uk.gov.hmcts.reform.dev.service.TaskService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:6969")
@RequestMapping("api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskEntity createTask(@Valid @RequestBody CreateTaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @GetMapping("/{id}")
    public TaskEntity getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping
    public Iterable<TaskEntity> getAllTasks() {
        return taskService.getAllTasksSortedByDueDateTime();
    }

    @PutMapping("{id}")
    public TaskEntity updateTask(@PathVariable Long id, @Validated @RequestBody UpdateTaskDto updateTaskDto) {
        return taskService.updateTask(id, updateTaskDto);
    }

    @PutMapping("{id}/status")
    public TaskEntity updateTaskStatus(@PathVariable Long id, @RequestBody TaskStatusDto newStatusDto) {

        return taskService.updateTaskStatus(id, newStatusDto);
    }

    @DeleteMapping("{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }


    @DeleteMapping("/delete-all")
    public void deleteAllTasks() {
        taskService.deleteAllTasks();
    }

}
