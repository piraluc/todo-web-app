package ch.cern.todo.repositories;

import ch.cern.todo.core.Task;
import ch.cern.todo.core.TaskCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class TaskRepositoryTests {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void When_TaskIsSaved_Then_TaskCanBeFound() {
        // Arrange
        final var deadline = LocalDateTime.parse("2023-09-22T10:42:50.63");
        final var category = new TaskCategory("some_name", "some_description");
        final var task = new Task("some_name", "some_description", deadline, category);

        // Act
        final var taskId = taskRepository.save(task).getId();

        // Assert
        assertTrue(taskRepository.findById(taskId).isPresent());
    }

    @Test
    void When_TaskIsSaved_Then_TaskPropertiesAreEqual() {
        // Arrange
        final var deadline = LocalDateTime.parse("2023-09-22T10:42:50.63");
        final var category = new TaskCategory("some_category_name", "some_category_description");
        final var task = new Task("some_task_name", "some_task_description", deadline, category);

        // Act
        final var taskId = taskRepository.save(task).getId();

        // Assert
        final var savedTask = taskRepository.findById(taskId).orElseThrow();

        assertEquals(savedTask.getName(), "some_task_name");
        assertEquals(savedTask.getDescription(), "some_task_description");
        assertEquals(savedTask.getDeadline(), deadline);
        assertEquals(savedTask.getCategory().getName(), category.getName());
        assertEquals(savedTask.getCategory().getDescription(), category.getDescription());
    }

    @Test
    void When_MultipleTaskAreSaved_Then_AllAreFound() {
        // Arrange
        for (int i = 0; i < 3; i++) {
            final var deadline = LocalDateTime.parse("2023-09-22T10:42:50.63");
            final var category = new TaskCategory("some_category_name_" + i, "some_category_description");
            final var task = new Task("some_task_name", "some_task_description", deadline, category);
            taskRepository.save(task);
        }

        // Act
        final var numberOfTasks = taskRepository.count();

        // Assert
        assertEquals(numberOfTasks, 3);
    }

    @Test
    void When_TaskIsUpdated_Then_PropertiesAreUpdated() {
        // Arrange
        final var deadline = LocalDateTime.parse("2023-09-22T10:42:50.63");
        final var updatedDeadline = deadline.plusHours(42);
        final var category = new TaskCategory("some_name", "some_description");
        final var updatedCategory = new TaskCategory("some_other_name", "some_other_description");
        final var task = new Task("some_name", "some_description", deadline, category);
        final var taskId = taskRepository.save(task).getId();

        // Act
        final var taskToUpdate = taskRepository.findById(taskId).orElseThrow();
        taskToUpdate.setName("some_updated_name");
        taskToUpdate.setDescription("some_updated_description");
        taskToUpdate.setDeadline(updatedDeadline);
        taskToUpdate.setCategory(updatedCategory);
        taskRepository.save(taskToUpdate);

        // Assert
        final var updatedTask = taskRepository.findById(taskId).orElseThrow();

        assertEquals(updatedTask.getName(), "some_updated_name");
        assertEquals(updatedTask.getDescription(), "some_updated_description");
        assertEquals(updatedTask.getDeadline(), updatedDeadline);
        assertEquals(updatedTask.getCategory().getName(), updatedCategory.getName());
        assertEquals(updatedTask.getCategory().getDescription(), updatedCategory.getDescription());
    }

    @Test
    void When_TaskIsDeleted_Then_TaskCannotBeFound() {
        // Arrange
        final var deadline = LocalDateTime.parse("2023-09-22T10:42:50.63");
        final var category = new TaskCategory("some_category_name", "some_category_description");
        final var task = new Task("some_task_name", "some_task_description", deadline, category);
        final var savedTaskId = taskRepository.save(task).getId();

        // Act
        taskRepository.deleteById(savedTaskId);

        // Assert
        assertTrue(taskRepository.findById(savedTaskId).isEmpty());
    }
}
