package ch.cern.todo.core;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskCategoryTests {

    @Test
    void When_TaskCategoryIsCreated_Then_NameIsReturned() {
        // Act
        final var taskCategory = new TaskCategory("some_name", null);

        // Assert
        final var taskCategoryName = taskCategory.getName();
        assertEquals(taskCategoryName, "some_name");
    }

    @Test
    void When_TaskCategoryIsCreated_And_NameIsNull_Then_IllegalArgumentExceptionIsThrown() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new TaskCategory(null, null));
    }

    @Test
    void When_TaskCategoryIsCreated_And_NameIsEmpty_Then_IllegalArgumentExceptionIsThrown() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new TaskCategory("", null));
    }

    @Test
    void When_TaskCategoryIsCreated_And_NameIsBlank_Then_IllegalArgumentExceptionIsThrown() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new TaskCategory("   ", null));
    }

    @Test
    void When_TaskCategoryIsCreated_Then_DescriptionIsReturned() {
        // Arrange & Act
        final var taskCategory = new TaskCategory("some_name", "some_description");

        // Assert
        final var taskCategoryDescription = taskCategory.getDescription();
        assertEquals(taskCategoryDescription, "some_description");
    }

    @Test
    void When_TaskCategoryIsCreated_And_DescriptionIsNull_Then_NoExceptionIsThrown() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            new TaskCategory("some_name", null);
        });
    }

    @Test
    void When_NameIsSet_Then_NameIsReturned() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");

        // Act
        taskCategory.setName("some_other_name");

        // Assert
        final var taskCategoryName = taskCategory.getName();
        assertEquals(taskCategoryName, "some_other_name");
    }

    @Test
    void When_NameIsSetToEmptyString_Then_IllegalArgumentExceptionIsThrown() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> taskCategory.setName(""));
    }

    @Test
    void When_NameIsSetToBlankString_Then_IllegalArgumentExceptionIsThrown() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> taskCategory.setName("     "));
    }

    @Test
    void When_NameIsSetToNull_Then_IllegalArgumentExceptionIsThrown() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> taskCategory.setName(null));
    }

    @Test
    void When_DescriptionIsSet_Then_DescriptionIsReturned() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");

        // Act
        taskCategory.setDescription("some_other_description");

        // Assert
        final var taskCategoryDescription = taskCategory.getDescription();
        assertEquals(taskCategoryDescription, "some_other_description");
    }
}
