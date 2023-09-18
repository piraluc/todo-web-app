package ch.cern.todo.services;

import ch.cern.todo.core.Task;
import ch.cern.todo.services.exceptions.TaskCategoryNotFoundException;
import ch.cern.todo.services.exceptions.TaskNotFoundException;

import java.util.List;

public interface TaskService {
    Task createTask(Task task) throws TaskCategoryNotFoundException;

    Task getTaskById(Long id) throws TaskNotFoundException;

    List<Task> getTasks();

    Task updateTask(Long id, Task task) throws TaskNotFoundException, TaskCategoryNotFoundException;

    void deleteTask(Long id) throws TaskNotFoundException;
}
