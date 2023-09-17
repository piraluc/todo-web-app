package ch.cern.todo.mappers;

import ch.cern.todo.api.models.TaskCategoryRequestModel;
import ch.cern.todo.api.models.TaskCategoryResponseModel;
import ch.cern.todo.core.TaskCategory;

public interface TaskCategoryMapper {

    TaskCategory mapToTaskCategory(TaskCategoryRequestModel taskCategoryRequest);

    TaskCategoryResponseModel mapToTaskCategoryResponseModel(TaskCategory taskCategory);
}
