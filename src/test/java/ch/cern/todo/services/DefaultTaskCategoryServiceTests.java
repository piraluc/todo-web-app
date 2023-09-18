package ch.cern.todo.services;

import ch.cern.todo.core.TaskCategory;
import ch.cern.todo.repositories.TaskCategoryRepository;
import ch.cern.todo.services.exceptions.DuplicateTaskCategoryNameException;
import ch.cern.todo.services.exceptions.TaskCategoryNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Optional;

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
    void When_TaskCategoryIsCreated_Then_TaskCategoryRepositorySaveIsCalled() throws DuplicateTaskCategoryNameException {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");

        // Act
        taskCategoryService.createTaskCategory(taskCategory);

        // Assert
        verify(taskCategoryRepository, times(1)).save(taskCategory);
    }

    @Test
    void When_TaskCategoryIsCreated_Then_RightTaskCategoryIsReturned() throws DuplicateTaskCategoryNameException {
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
    void When_TaskCategoryIsCreatedWithSameName_Then_DuplicateTaskCategoryNameExceptionIsThrown() {
        // Arrange
        final var taskCategoryWithSameName = new TaskCategory("some_name", "some_description");

        when(taskCategoryRepository.existsByName("some_name")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateTaskCategoryNameException.class, () -> taskCategoryService.createTaskCategory(taskCategoryWithSameName));
    }

    @Test
    void When_TaskCategoryIsRequestedById_Then_RightTaskCategoryIsReturned() throws TaskCategoryNotFoundException {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");
        taskCategory.setId(42);

        when(taskCategoryRepository.findById(42L)).thenReturn(Optional.of(taskCategory));

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
        when(taskCategoryRepository.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskCategoryNotFoundException.class, () -> taskCategoryService.getTaskCategoryById(42L));
    }

    @Test
    void When_TaskCategoryIsRequestedByName_Then_RightTaskCategoryIsReturned() throws TaskCategoryNotFoundException {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");
        taskCategory.setId(42);

        when(taskCategoryRepository.existsByName("some_name")).thenReturn(true);
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
        assertThrows(TaskCategoryNotFoundException.class, () -> taskCategoryService.getTaskCategoryByName("some_name"));
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
    void When_TaskCategoryIsUpdated_Then_TaskCategoryRepositorySaveIsCalled() throws TaskCategoryNotFoundException {
        // Arrange
        final var taskCategoryWithUpdates = new TaskCategory("some_new_name", "some_new_description");
        when(taskCategoryRepository.findById(42L)).thenReturn(Optional.of(taskCategoryWithUpdates));

        // Act
        taskCategoryService.updateTaskCategory(42L, taskCategoryWithUpdates);

        // Assert
        verify(taskCategoryRepository, times(1)).save(taskCategoryWithUpdates);
    }

    @Test
    void When_TaskCategoryIsUpdated_Then_UpdatedTaskCategoryIsReturned() throws TaskCategoryNotFoundException {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");
        taskCategory.setId(42);

        final var taskCategoryWithUpdates = new TaskCategory("some_new_name", "some_new_description");

        when(taskCategoryRepository.findById(42L)).thenReturn(Optional.of(taskCategory));
        when(taskCategoryRepository.save(taskCategory)).thenReturn(taskCategory);

        // Act
        final var updatedTaskCategory = taskCategoryService.updateTaskCategory(42L, taskCategoryWithUpdates);

        // Assert
        assertEquals(updatedTaskCategory.getId(), 42);
        assertEquals(updatedTaskCategory.getName(), "some_new_name");
        assertEquals(updatedTaskCategory.getDescription(), "some_new_description");
    }

    @Test
    void When_TaskCategoryIsDeleted_Then_TaskCategoryRepositoryDeleteByIdIsCalled() throws TaskCategoryNotFoundException {
        // Arrange
        when(taskCategoryRepository.existsById(42L)).thenReturn(true);

        // Act
        taskCategoryService.deleteTaskCategory(42L);

        // Assert
        verify(taskCategoryRepository, times(1)).deleteById(42L);
    }

    @Test
    void When_TaskCategoryIsDeletedNotExists_Then_ExceptionIsThrown() {
        // Arrange
        when(taskCategoryRepository.existsById(42L)).thenReturn(false);

        // Act & Assert
        assertThrows(TaskCategoryNotFoundException.class, () -> taskCategoryService.deleteTaskCategory(42L));
    }
}
