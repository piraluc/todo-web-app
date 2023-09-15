package ch.cern.todo.services;

import ch.cern.todo.core.Task;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);

    Task getTaskById(Long id);

    List<Task> getTasks();

    Task updateTask(Long id, Task task);

    void deleteTask(Long id);
}
