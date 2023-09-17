package ch.cern.todo.mappers;

import ch.cern.todo.api.models.TaskCategoryRequestModel;
import ch.cern.todo.core.TaskCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DefaultTaskCategoryMapperTests {

    @Autowired
    private DefaultTaskCategoryMapper taskCategoryMapper;

    @Test
    void When_IsMappedToTaskCategory_Then_AllPropertiesAreSet() {
        // Arrange
        final var taskCategoryRequest = new TaskCategoryRequestModel("some_name", "some_description");

        // Act
        final var taskCategory = taskCategoryMapper.mapToTaskCategory(taskCategoryRequest);

        // Assert
        assertEquals(taskCategory.getName(), "some_name");
        assertEquals(taskCategory.getDescription(), "some_description");
    }

    @Test
    void When_IsMappedToTaskCategoryResponseModel_Then_AllPropertiesAreSet() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");
        taskCategory.setId(42);

        // Act
        final var taskCategoryResponse = taskCategoryMapper.mapToTaskCategoryResponseModel(taskCategory);

        // Assert
        assertEquals(taskCategoryResponse.getId(), 42);
        assertEquals(taskCategoryResponse.getName(), "some_name");
        assertEquals(taskCategoryResponse.getDescription(), "some_description");
    }
}
