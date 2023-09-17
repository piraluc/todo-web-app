package ch.cern.todo.mappers;

import ch.cern.todo.api.models.TaskRequestModel;
import ch.cern.todo.api.models.TaskResponseModel;
import ch.cern.todo.core.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultTaskMapper implements TaskMapper {
    
    private final TaskCategoryMapper taskCategoryMapper;

    @Autowired
    public DefaultTaskMapper(TaskCategoryMapper taskCategoryMapper) {
        this.taskCategoryMapper = taskCategoryMapper;
    }

    @Override
    public Task mapToTask(TaskRequestModel taskRequest) {
        final var taskCategory = taskCategoryMapper.mapToTaskCategory(taskRequest.getCategory());
        return new Task(taskRequest.getName(), taskRequest.getDescription(), taskRequest.getDeadline(), taskCategory);
    }

    @Override
    public TaskResponseModel mapToTaskResponse(Task task) {
        final var taskCategoryResponse = taskCategoryMapper.mapToTaskCategoryResponseModel(task.getCategory());
        return new TaskResponseModel(task.getId(), task.getName(), task.getDescription(), task.getDeadline(), taskCategoryResponse);
    }
}
