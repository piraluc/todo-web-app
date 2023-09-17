package ch.cern.todo.mappers;

import ch.cern.todo.api.models.TaskCategoryRequestModel;
import ch.cern.todo.api.models.TaskCategoryResponseModel;
import ch.cern.todo.core.TaskCategory;
import org.springframework.stereotype.Component;

@Component
public class DefaultTaskCategoryMapper implements TaskCategoryMapper {

    @Override
    public TaskCategory mapToTaskCategory(TaskCategoryRequestModel request) {
        return new TaskCategory(request.getName(), request.getDescription());
    }

    @Override
    public TaskCategoryResponseModel mapToTaskCategoryResponseModel(TaskCategory taskCategory) {
        return new TaskCategoryResponseModel(taskCategory.getId(), taskCategory.getName(), taskCategory.getDescription());
    }
}
