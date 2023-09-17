package ch.cern.todo.services;

import ch.cern.todo.core.TaskCategory;
import ch.cern.todo.repositories.TaskCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultTaskCategoryService implements TaskCategoryService {

    private final TaskCategoryRepository taskCategoryRepository;

    @Autowired
    public DefaultTaskCategoryService(TaskCategoryRepository taskCategoryRepository) {
        this.taskCategoryRepository = taskCategoryRepository;
    }

    @Override
    public TaskCategory createTaskCategory(TaskCategory taskCategory) throws DuplicateTaskCategoryNameException {
        if (taskCategoryRepository.existsByName(taskCategory.getName())) {
            throw new DuplicateTaskCategoryNameException();
        }

        return taskCategoryRepository.save(taskCategory);
    }

    @Override
    public TaskCategory getTaskCategoryById(Long id) throws TaskCategoryNotFoundException {
        final var taskCategory = taskCategoryRepository.findById(id);
        if (taskCategory.isEmpty()) {
            throw new TaskCategoryNotFoundException();
        }

        return taskCategory.get();
    }

    @Override
    public TaskCategory getTaskCategoryByName(String name) throws TaskCategoryNotFoundException {
        if (!taskCategoryRepository.existsByName(name)) {
            throw new TaskCategoryNotFoundException();
        }

        return taskCategoryRepository.findByName(name);
    }

    @Override
    public List<TaskCategory> getTaskCategories() {
        return taskCategoryRepository.findAll();
    }

    @Override
    public TaskCategory updateTaskCategory(Long id, TaskCategory taskCategory) throws TaskCategoryNotFoundException {
        final var taskCategoryToUpdate = getTaskCategoryById(id);
        taskCategoryToUpdate.setName(taskCategory.getName());
        taskCategoryToUpdate.setDescription(taskCategory.getDescription());

        return taskCategoryRepository.save(taskCategoryToUpdate);
    }

    @Override
    public void deleteTaskCategory(Long id) throws TaskCategoryNotFoundException {
        if (!taskCategoryRepository.existsById(id)) {
            throw new TaskCategoryNotFoundException();
        }

        taskCategoryRepository.deleteById(id);
    }
}
