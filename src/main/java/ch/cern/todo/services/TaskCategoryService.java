package ch.cern.todo.services;

import ch.cern.todo.core.TaskCategory;

import java.util.List;

public interface TaskCategoryService {
    TaskCategory createTaskCategory(TaskCategory taskCategory);

    TaskCategory getTaskCategoryById(Long id);

    TaskCategory getTaskCategoryByName(String name);

    List<TaskCategory> getTaskCategories();

    TaskCategory updateTaskCategory(Long id, TaskCategory taskCategory);

    void deleteTaskCategory(Long id);
}
