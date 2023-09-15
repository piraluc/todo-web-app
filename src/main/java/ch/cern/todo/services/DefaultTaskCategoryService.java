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
    public TaskCategory createTaskCategory(TaskCategory taskCategory) {
        return taskCategoryRepository.save(taskCategory);
    }

    @Override
    public TaskCategory getTaskCategoryById(Long id) {
        return taskCategoryRepository.getById(id);
    }

    @Override
    public TaskCategory getTaskCategoryByName(String name) {
        return taskCategoryRepository.findByName(name);
    }

    @Override
    public List<TaskCategory> getTaskCategories() {
        return taskCategoryRepository.findAll();
    }

    @Override
    public TaskCategory updateTaskCategory(Long id, TaskCategory taskCategory) {
        final var taskCategoryToUpdate = taskCategoryRepository.getById(id);

        taskCategoryToUpdate.setName(taskCategory.getName());
        taskCategoryToUpdate.setDescription(taskCategory.getDescription());

        return taskCategoryRepository.save(taskCategoryToUpdate);
    }

    @Override
    public void deleteTaskCategory(Long id) {
        taskCategoryRepository.deleteById(id);
    }
}
