package ch.cern.todo.api.controllers;

import ch.cern.todo.api.models.TaskCategoryRequestModel;
import ch.cern.todo.api.models.TaskCategoryResponseModel;
import ch.cern.todo.core.TaskCategory;
import ch.cern.todo.mappers.TaskCategoryMapper;
import ch.cern.todo.services.TaskCategoryService;
import ch.cern.todo.services.exceptions.DuplicateTaskCategoryNameException;
import ch.cern.todo.services.exceptions.TaskCategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/categories")
public class TaskCategoryController {

    private final TaskCategoryService taskCategoryService;

    private final TaskCategoryMapper taskCategoryMapper;

    @Autowired
    public TaskCategoryController(TaskCategoryService taskCategoryService, TaskCategoryMapper taskCategoryMapper) {
        this.taskCategoryService = taskCategoryService;
        this.taskCategoryMapper = taskCategoryMapper;
    }

    @PostMapping
    public TaskCategoryResponseModel createTaskCategory(@RequestBody TaskCategoryRequestModel taskCategoryRequest) {
        final var mappedTaskCategory = taskCategoryMapper.mapToTaskCategory(taskCategoryRequest);
        final TaskCategory createdTaskCategory;
        final TaskCategoryResponseModel taskCategoryResponse;

        try {
            createdTaskCategory = taskCategoryService.createTaskCategory(mappedTaskCategory);
        } catch (DuplicateTaskCategoryNameException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        taskCategoryResponse = taskCategoryMapper.mapToTaskCategoryResponseModel(createdTaskCategory);
        return taskCategoryResponse;
    }

    @GetMapping("/{id}")
    public TaskCategoryResponseModel getTaskCategory(@PathVariable Long id) {
        final TaskCategory taskCategory;
        try {
            taskCategory = taskCategoryService.getTaskCategoryById(id);
        } catch (TaskCategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

        return taskCategoryMapper.mapToTaskCategoryResponseModel(taskCategory);
    }

    @GetMapping
    public List<TaskCategoryResponseModel> getTaskCategories() {
        final var taskCategories = taskCategoryService.getTaskCategories();
        return taskCategories.stream().map(taskCategoryMapper::mapToTaskCategoryResponseModel).toList();
    }

    @PutMapping("/{id}")
    public TaskCategoryResponseModel updateTaskCategory(@PathVariable Long id, @RequestBody TaskCategoryRequestModel taskCategoryRequest) {
        final var taskCategory = taskCategoryMapper.mapToTaskCategory(taskCategoryRequest);
        final TaskCategory updatedTaskCategory;
        try {
            updatedTaskCategory = taskCategoryService.updateTaskCategory(id, taskCategory);
        } catch (TaskCategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

        return taskCategoryMapper.mapToTaskCategoryResponseModel(updatedTaskCategory);
    }

    @DeleteMapping("/{id}")
    public void deleteTaskCategory(@PathVariable Long id) {
        try {
            taskCategoryService.deleteTaskCategory(id);
        } catch (TaskCategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
