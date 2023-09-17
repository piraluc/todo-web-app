package ch.cern.todo.mappers;

import ch.cern.todo.api.models.TaskCategoryRequestModel;
import ch.cern.todo.api.models.TaskRequestModel;
import ch.cern.todo.core.Task;
import ch.cern.todo.core.TaskCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DefaultTaskMapperTests {

    @Autowired
    private DefaultTaskMapper taskMapper;

    @Test
    void When_IsMappedToTask_Then_AllPropertiesAreSet() {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var taskCategoryRequest = new TaskCategoryRequestModel("some_category_name", "some_category_description");
        final var taskRequest = new TaskRequestModel("some_name", "some_description", deadline, taskCategoryRequest);

        // Act
        final var task = taskMapper.mapToTask(taskRequest);

        // Assert
        assertEquals(task.getName(), "some_name");
        assertEquals(task.getDescription(), "some_description");
        assertEquals(task.getDeadline(), deadline);
        assertEquals(task.getCategory().getName(), "some_category_name");
        assertEquals(task.getCategory().getDescription(), "some_category_description");
    }

    @Test
    void When_IsMappedToTaskResponseModel_Then_AllPropertiesAreSet() {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var taskCategory = new TaskCategory("some_category_name", "some_category_description");
        taskCategory.setId(73);
        final var task = new Task("some_name", "some_description", deadline, taskCategory);
        task.setId(42);

        // Act
        final var taskResponse = taskMapper.mapToTaskResponse(task);

        // Assert
        assertEquals(taskResponse.getId(), 42);
        assertEquals(taskResponse.getName(), "some_name");
        assertEquals(taskResponse.getDescription(), "some_description");
        assertEquals(taskResponse.getDeadline(), deadline);
        assertEquals(taskResponse.getCategory().getId(), 73);
        assertEquals(taskResponse.getCategory().getName(), "some_category_name");
        assertEquals(taskResponse.getCategory().getDescription(), "some_category_description");
    }
}
