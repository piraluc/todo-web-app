package ch.cern.todo.services;

import ch.cern.todo.core.Task;
import ch.cern.todo.services.exceptions.TaskNotFoundException;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);

    Task getTaskById(Long id) throws TaskNotFoundException;

    List<Task> getTasks();

    Task updateTask(Long id, Task task);

    void deleteTask(Long id);
}
