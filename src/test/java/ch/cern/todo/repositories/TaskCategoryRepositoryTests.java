package ch.cern.todo.repositories;

import ch.cern.todo.core.TaskCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TaskCategoryRepositoryTests {

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;

    @Test
    void When_TaskCategoryIsSaved_Then_TaskCategoryCanBeFound() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");

        // Act
        final var savedTaskCategoryId = taskCategoryRepository.save(taskCategory).getId();

        // Assert
        assertTrue(taskCategoryRepository.findById(savedTaskCategoryId).isPresent());
    }

    @Test
    void When_TaskCategoryIsSaved_Then_TaskCategoryPropertiesAreEqual() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");

        // Act
        final var savedTaskCategoryId = taskCategoryRepository.save(taskCategory).getId();

        // Assert
        final var savedTaskCategory = taskCategoryRepository.findById(savedTaskCategoryId).orElseThrow();

        assertEquals(savedTaskCategory.getName(), "some_name");
        assertEquals(savedTaskCategory.getDescription(), "some_description");
    }

    @Test
    void When_TaskCategoryWithSameNameIsSaved_Then_DataIntegrityViolationExceptionIsThrown() {
        // Arrange
        final var taskCategoryA = new TaskCategory("same_name", "some_description");
        final var taskCategoryB = new TaskCategory("same_name", "some_description");

        taskCategoryRepository.save(taskCategoryA);

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> taskCategoryRepository.save(taskCategoryB));
    }

    @Test
    void When_MultipleTaskCategoriesAreSaved_Then_AllAreFound() {
        // Arrange
        for (int i = 0; i < 3; i++) {
            final var taskCategory = new TaskCategory("some_category_name_" + i, "some_category_description");
            taskCategoryRepository.save(taskCategory);
        }

        // Act
        final var numberOfTaskCategories = taskCategoryRepository.count();

        // Assert
        assertEquals(numberOfTaskCategories, 3);
    }

    @Test
    void When_TaskCategoryIsUpdated_Then_PropertiesAreUpdated() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");
        final var taskCategoryId = taskCategoryRepository.save(taskCategory).getId();
        final var taskCategoryToUpdate = taskCategoryRepository.findById(taskCategoryId).orElseThrow();

        // Act
        taskCategoryToUpdate.setName("some_new_name");
        taskCategoryToUpdate.setDescription("some_new_description");

        taskCategoryRepository.save(taskCategoryToUpdate);

        // Assert
        final var updatedTaskCategory = taskCategoryRepository.findById(taskCategoryId).orElseThrow();
        assertEquals(updatedTaskCategory.getName(), "some_new_name");
        assertEquals(updatedTaskCategory.getDescription(), "some_new_description");
    }

    @Test
    void When_TaskCategoryIsDeleted_Then_TaskCategoryCannotBeFound() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");
        final var taskCategoryId = taskCategoryRepository.save(taskCategory).getId();

        // Act
        taskCategoryRepository.deleteById(taskCategoryId);

        // Assert
        assertTrue(taskCategoryRepository.findById(taskCategoryId).isEmpty());
    }

    @Test
    void When_TaskCategoryIsSearchedByName_Then_TaskCategoryCanBeFound() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");
        final var savedTaskCategoryId = taskCategoryRepository.save(taskCategory).getId();

        // Act
        final var foundTaskCategory = taskCategoryRepository.findByName("some_name");

        // Assert
        assertEquals(foundTaskCategory.getId(), savedTaskCategoryId);
        assertEquals(foundTaskCategory.getName(), "some_name");
        assertEquals(foundTaskCategory.getDescription(), "some_description");
    }
}
