package ch.cern.todo.mappers;

import ch.cern.todo.api.models.TaskRequestModel;
import ch.cern.todo.api.models.TaskResponseModel;
import ch.cern.todo.core.Task;

public interface TaskMapper {

    Task mapToTask(TaskRequestModel taskRequest);

    TaskResponseModel mapToTaskResponse(Task task);
}
