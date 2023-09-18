package ch.cern.todo.api.controllers;

import ch.cern.todo.api.models.TaskRequestModel;
import ch.cern.todo.api.models.TaskResponseModel;
import ch.cern.todo.core.Task;
import ch.cern.todo.mappers.TaskMapper;
import ch.cern.todo.services.TaskService;
import ch.cern.todo.services.exceptions.TaskCategoryNotFoundException;
import ch.cern.todo.services.exceptions.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;


    @Autowired
    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping
    public TaskResponseModel createTask(@RequestBody TaskRequestModel taskRequest) {
        final var task = taskMapper.mapToTask(taskRequest);
        try {
            final var createdTask = taskService.createTask(task);
            return taskMapper.mapToTaskResponse(createdTask);
        } catch (TaskCategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{id}")
    public TaskResponseModel getTask(@PathVariable Long id) {
        final Task task;
        try {
            task = taskService.getTaskById(id);
        } catch (TaskNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

        return taskMapper.mapToTaskResponse(task);
    }

    @GetMapping
    public List<TaskResponseModel> getTasks() {
        final var tasks = taskService.getTasks();
        return tasks.stream().map(taskMapper::mapToTaskResponse).toList();
    }

    @PutMapping("/{id}")
    public TaskResponseModel updateTask(@PathVariable Long id, @RequestBody TaskRequestModel taskRequest) {
        final var task = taskMapper.mapToTask(taskRequest);
        final Task updatedTask;
        try {
            updatedTask = taskService.updateTask(id, task);
        } catch (TaskNotFoundException | TaskCategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

        return taskMapper.mapToTaskResponse(updatedTask);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
        } catch (TaskNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
