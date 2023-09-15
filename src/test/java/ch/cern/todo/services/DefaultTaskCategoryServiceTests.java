package ch.cern.todo.services;

import ch.cern.todo.core.TaskCategory;
import ch.cern.todo.repositories.TaskCategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DefaultTaskCategoryServiceTests {

    @InjectMocks
    private DefaultTaskCategoryService taskCategoryService;

    @Spy
    private TaskCategoryRepository taskCategoryRepository;

    private AutoCloseable closeable;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void closeMocks() throws Exception {
        closeable.close();
    }

    @Test
    void When_TaskCategoryIsCreated_Then_TaskCategoryRepositorySaveIsCalled() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");

        // Act
        taskCategoryService.createTaskCategory(taskCategory);

        // Assert
        verify(taskCategoryRepository, times(1)).save(taskCategory);
    }

    @Test
    void When_TaskCategoryIsCreated_Then_RightTaskCategoryIsReturned() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");
        taskCategory.setId(42);

        when(taskCategoryRepository.save(taskCategory)).thenReturn(taskCategory);

        // Act
        final var createdTaskCategory = taskCategoryService.createTaskCategory(taskCategory);

        // Assert
        assertEquals(createdTaskCategory.getId(), 42);
        assertEquals(createdTaskCategory.getName(), "some_name");
        assertEquals(createdTaskCategory.getDescription(), "some_description");
    }

    @Test
    void When_TaskCategoryIsRequestedById_Then_RightTaskCategoryIsReturned() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");
        taskCategory.setId(42);

        when(taskCategoryRepository.getById(42L)).thenReturn(taskCategory);

        // Act
        final var createdTaskCategory = taskCategoryService.getTaskCategoryById(42L);

        // Assert
        assertEquals(createdTaskCategory.getId(), 42);
        assertEquals(createdTaskCategory.getName(), "some_name");
        assertEquals(createdTaskCategory.getDescription(), "some_description");
    }

    @Test
    void When_TaskCategoryIsRequestedByIdDoesNotExist_Then_ExceptionIsThrown() {
        // Arrange
        when(taskCategoryRepository.getById(any())).thenThrow(new EntityNotFoundException());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> taskCategoryService.getTaskCategoryById(42L));
    }

    @Test
    void When_TaskCategoryIsRequestedByName_Then_RightTaskCategoryIsReturned() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");
        taskCategory.setId(42);

        when(taskCategoryRepository.findByName("some_name")).thenReturn(taskCategory);

        // Act
        final var createdTaskCategory = taskCategoryService.getTaskCategoryByName("some_name");

        // Assert
        assertEquals(createdTaskCategory.getId(), 42);
        assertEquals(createdTaskCategory.getName(), "some_name");
        assertEquals(createdTaskCategory.getDescription(), "some_description");
    }

    @Test
    void When_TaskCategoryIsRequestedByNameDoesNotExist_Then_ExceptionIsThrown() {
        // Arrange
        when(taskCategoryRepository.findByName(any())).thenThrow(new EntityNotFoundException());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> taskCategoryService.getTaskCategoryByName("some_name"));
    }

    @Test
    void When_AllTaskCategoriesAreRequested_Then_AllTaskCategoriesAreReturned() {
        // Arrange
        final var taskCategoryA = new TaskCategory("some_name", "some_description");
        taskCategoryA.setId(42);
        final var taskCategoryB = new TaskCategory("some_different_name", "some_different_description");
        taskCategoryB.setId(73);

        when(taskCategoryRepository.findAll()).thenReturn(Arrays.asList(taskCategoryA, taskCategoryB));

        // Act
        final var taskCategories = taskCategoryService.getTaskCategories();

        // Assert
        assertEquals(taskCategories.size(), 2);
        assertTrue(taskCategories.contains(taskCategoryA));
        assertTrue(taskCategories.contains(taskCategoryB));
    }

    @Test
    void When_TaskCategoryIsUpdated_Then_TaskCategoryRepositorySaveIsCalled() {
        // Arrange
        final var taskCategoryWithUpdates = new TaskCategory("some_new_name", "some_new_description");
        when(taskCategoryRepository.getById(42L)).thenReturn(taskCategoryWithUpdates);

        // Act
        taskCategoryService.updateTaskCategory(42L, taskCategoryWithUpdates);

        // Assert
        verify(taskCategoryRepository, times(1)).save(taskCategoryWithUpdates);
    }

    @Test
    void When_TaskCategoryIsUpdated_Then_UpdatedTaskCategoryIsReturned() {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");
        taskCategory.setId(42);

        final var taskCategoryWithUpdates = new TaskCategory("some_new_name", "some_new_description");

        when(taskCategoryRepository.getById(42L)).thenReturn(taskCategory);
        when(taskCategoryRepository.save(taskCategory)).thenReturn(taskCategory);

        // Act
        final var updatedTaskCategory = taskCategoryService.updateTaskCategory(42L, taskCategoryWithUpdates);

        // Assert
        assertEquals(updatedTaskCategory.getId(), 42);
        assertEquals(updatedTaskCategory.getName(), "some_new_name");
        assertEquals(updatedTaskCategory.getDescription(), "some_new_description");
    }

    @Test
    void When_TaskCategoryIsDeleted_Then_TaskCategoryRepositoryDeleteByIdIsCalled() {
        // Act
        taskCategoryService.deleteTaskCategory(42L);

        // Assert
        verify(taskCategoryRepository, times(1)).deleteById(42L);
    }
}
