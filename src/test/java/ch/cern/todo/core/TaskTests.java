package ch.cern.todo.core;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskTests {
    @Test
    void When_TaskIsCreated_Then_NameIsReturned() {
        // Arrange
        final LocalDateTime deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", "some_description");

        // Act
        final var task = new Task("some_name", "some_description", deadline, category);

        // Assert
        final var taskName = task.getName();
        assertEquals(taskName, "some_name");
    }

    @Test
    void When_TaskIsCreated_And_NameIsNull_Then_IllegalArgumentExceptionIsThrown() {
        // Arrange
        final LocalDateTime deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Task(null, "some_description", deadline, category);
        });
    }

    @Test
    void When_TaskIsCreated_And_NameIsEmpty_Then_IllegalArgumentExceptionIsThrown() {
        // Arrange
        final LocalDateTime deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Task("", "some_description", deadline, category);
        });
    }

    @Test
    void When_TaskIsCreated_And_NameIsBlank_Then_IllegalArgumentExceptionIsThrown() {
        // Arrange
        final LocalDateTime deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Task("   ", "some_description", deadline, category);
        });
    }

    @Test
    void When_TaskIsCreated_Then_DescriptionIsReturned() {
        // Arrange
        final LocalDateTime deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", null);

        // Act
        final var task = new Task("some_name", "some_description", deadline, category);

        // Assert
        final var taskDescription = task.getDescription();
        assertEquals("some_description", taskDescription);
    }

    @Test
    void When_TaskIsCreated_And_DescriptionIsNull_Then_NoExceptionIsThrown() {
        // Arrange
        final LocalDateTime deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", null);

        // Act & Assert
        assertDoesNotThrow(() -> {
            new Task("some_name", null, deadline, category);
        });
    }

    @Test
    void When_TaskIsCreated_Then_DeadlineIsReturned() {
        // Arrange
        final var deadline = LocalDateTime.parse("2023-09-22T10:42:50.63");
        final var category = new TaskCategory("some_name", null);

        // Act
        final var task = new Task("some_name", "some_description", deadline, category);

        // Assert
        final var taskDeadline = task.getDeadline();
        assertEquals(deadline, taskDeadline);
    }

    @Test
    void When_TaskIsCreated_And_DeadlineIsNull_Then_IllegalArgumentExceptionIsThrown() {
        // Arrange
        final var category = new TaskCategory("some_name", null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Task("some_name", "some_description", null, category);
        });
    }

    @Test
    void When_TaskIsCreated_Then_TaskCategoryIsReturned() {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", null);

        // Act
        final var task = new Task("some_name", "some_description", deadline, category);

        // Assert
        final var taskCategory = task.getCategory();
        assertEquals(category, taskCategory);
    }

    @Test
    void When_TaskIsCreated_And_TaskCategoryIsNull_Then_IllegalArgumentExceptionIsThrown() {
        // Arrange
        final var deadline = LocalDateTime.parse("2023-09-22T10:42:50.63");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Task("some_name", "some_description", deadline, null);
        });
    }

    @Test
    void When_NameIsSet_Then_NameIsReturned() {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", null);
        final var task = new Task("some_name", "some_description", deadline, category);

        // Act
        task.setName("some_other_name");

        // Assert
        final var name = task.getName();
        assertEquals(name, "some_other_name");
    }

    @Test
    void When_NameIsSetToEmptyString_Then_IllegalArgumentExceptionIsThrown() {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", null);
        final var task = new Task("some_name", "some_description", deadline, category);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            task.setName("");
        });
    }

    @Test
    void When_NameIsSetToBlankString_Then_IllegalArgumentExceptionIsThrown() {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", null);
        final var task = new Task("some_name", "some_description", deadline, category);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            task.setName("    ");
        });
    }

    @Test
    void When_NameIsSetToNull_Then_IllegalArgumentExceptionIsThrown() {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", null);
        final var task = new Task("some_name", "some_description", deadline, category);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            task.setName(null);
        });
    }

    @Test
    void When_DescriptionIsSetToNull_Then_NoExceptionIsThrown() {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", null);
        final var task = new Task("some_name", "some_description", deadline, category);

        // Act & Assert
        assertDoesNotThrow(() -> {
            task.setDescription(null);
        });
    }

    @Test
    void When_DeadlineIsSet_Then_DeadlineIsReturned() {
        // Arrange
        final var deadline = LocalDateTime.parse("2023-09-12T13:09:50.63");
        final var category = new TaskCategory("some_name", null);
        final var task = new Task("some_name", "some_description", deadline, category);

        // Act
        final var updatedDeadline = LocalDateTime.parse("2023-09-22T10:42:50.63");
        task.setDeadline(updatedDeadline);

        // Assert
        final var taskDeadline = task.getDeadline();
        assertEquals(taskDeadline, updatedDeadline);
    }

    @Test
    void When_DeadlineIsSetToNull_Then_IllegalArgumentExceptionIsThrown() {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", null);
        final var task = new Task("some_name", "some_description", deadline, category);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            task.setDeadline(null);
        });
    }

    @Test
    void When_CategoryIsSet_Then_CategoryIsReturned() {
        // Arrange
        final var deadline = LocalDateTime.parse("2023-09-12T13:09:50.63");
        final var category = new TaskCategory("some_name", null);
        final var task = new Task("some_name", "some_description", deadline, category);

        // Act
        final var updatedCategory = new TaskCategory("some_other_name", null);
        task.setCategory(updatedCategory);

        // Assert
        final var taskCategory = task.getCategory();
        assertEquals(taskCategory, updatedCategory);
    }

    @Test
    void When_CategoryIsSetToNull_Then_IllegalArgumentExceptionIsThrown() {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", null);
        final var task = new Task("some_name", "some_description", deadline, category);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            task.setCategory(null);
        });
    }
}
