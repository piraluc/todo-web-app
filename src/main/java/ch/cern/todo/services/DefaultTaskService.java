package ch.cern.todo.services;

import ch.cern.todo.core.Task;
import ch.cern.todo.repositories.TaskRepository;
import ch.cern.todo.services.exceptions.TaskCategoryNotFoundException;
import ch.cern.todo.services.exceptions.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultTaskService implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskCategoryService taskCategoryService;

    @Autowired
    public DefaultTaskService(TaskRepository taskRepository, TaskCategoryService taskCategoryService) {
        this.taskRepository = taskRepository;
        this.taskCategoryService = taskCategoryService;
    }

    @Override
    public Task createTask(Task task) throws TaskCategoryNotFoundException {
        String taskCategoryName = task.getCategory().getName();
        final var taskCategory = taskCategoryService.getTaskCategoryByName(taskCategoryName);

        task.setCategory(taskCategory);

        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long id) throws TaskNotFoundException {
        final var task = taskRepository.findById(id);
        if (task.isEmpty()) {
            throw new TaskNotFoundException();
        }

        return task.get();
    }

    @Override
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task updateTask(Long id, Task task) throws TaskNotFoundException {
        final var taskToUpdate = getTaskById(id);

        taskToUpdate.setName(task.getName());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setDeadline(task.getDeadline());
        taskToUpdate.setCategory(task.getCategory());

        return taskRepository.save(taskToUpdate);
    }

    @Override
    public void deleteTask(Long id) throws TaskNotFoundException {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException();
        }
        taskRepository.deleteById(id);
    }
}
