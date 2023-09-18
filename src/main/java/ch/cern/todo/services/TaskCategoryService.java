package ch.cern.todo.services;

import ch.cern.todo.core.TaskCategory;
import ch.cern.todo.services.exceptions.DuplicateTaskCategoryNameException;
import ch.cern.todo.services.exceptions.TaskCategoryNotFoundException;

import java.util.List;

public interface TaskCategoryService {
    TaskCategory createTaskCategory(TaskCategory taskCategory) throws DuplicateTaskCategoryNameException;

    TaskCategory getTaskCategoryById(Long id) throws TaskCategoryNotFoundException;

    TaskCategory getTaskCategoryByName(String name) throws TaskCategoryNotFoundException;

    List<TaskCategory> getTaskCategories();

    TaskCategory updateTaskCategory(Long id, TaskCategory taskCategory) throws TaskCategoryNotFoundException;

    void deleteTaskCategory(Long id) throws TaskCategoryNotFoundException;
}
