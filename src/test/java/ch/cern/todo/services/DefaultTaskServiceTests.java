package ch.cern.todo.services;

import ch.cern.todo.core.Task;
import ch.cern.todo.core.TaskCategory;
import ch.cern.todo.repositories.TaskRepository;
import ch.cern.todo.services.exceptions.TaskCategoryNotFoundException;
import ch.cern.todo.services.exceptions.TaskNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DefaultTaskServiceTests {

    @InjectMocks
    private DefaultTaskService taskService;

    @Spy
    private TaskRepository taskRepository;

    @Spy
    TaskCategoryService taskCategoryService;

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
    void When_TaskIsCreated_Then_TaskRepositorySaveIsCalled() throws TaskCategoryNotFoundException {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", "some_description");
        final var task = new Task("some_name", "some_description", deadline, category);

        when(taskCategoryService.getTaskCategoryByName("some_name")).thenReturn(category);

        // Act
        taskService.createTask(task);

        // Assert
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void When_TaskIsCreated_Then_RightTaskIsReturned() throws TaskCategoryNotFoundException {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", "some_description");
        final var task = new Task("some_name", "some_description", deadline, category);
        task.setId(42);

        when(taskCategoryService.getTaskCategoryByName("some_name")).thenReturn(category);
        when(taskRepository.save(task)).thenReturn(task);

        // Act
        final var createdTask = taskService.createTask(task);

        // Assert
        assertEquals(createdTask.getId(), 42);
        assertEquals(createdTask.getName(), "some_name");
        assertEquals(createdTask.getDescription(), "some_description");
        assertEquals(createdTask.getDeadline(), deadline);
        assertEquals(createdTask.getCategory(), category);
    }

    @Test
    void When_TaskIsCreatedAndTaskCategoryDoesNotExists_Then_TaskCategoryNotFoundExceptionIsThrown() throws TaskCategoryNotFoundException {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", "some_description");
        final var task = new Task("some_name", "some_description", deadline, category);

        when(taskCategoryService.getTaskCategoryByName("some_name")).thenThrow(new TaskCategoryNotFoundException());

        // Act & Assert
        assertThrows(TaskCategoryNotFoundException.class, () -> taskService.createTask(task));
    }

    @Test
    void When_TaskIsRequestedById_Then_RightTaskIsReturned() throws TaskNotFoundException {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", "some_description");
        final var task = new Task("some_name", "some_description", deadline, category);
        task.setId(42);

        when(taskRepository.findById(42L)).thenReturn(Optional.of(task));

        // Act
        final var createdTask = taskService.getTaskById(42L);

        // Assert
        assertEquals(createdTask.getId(), 42);
        assertEquals(createdTask.getName(), "some_name");
        assertEquals(createdTask.getDescription(), "some_description");
        assertEquals(createdTask.getDeadline(), deadline);
        assertEquals(createdTask.getCategory(), category);
    }

    @Test
    void When_TaskIsRequestedByIdDoesNotExist_Then_TaskNotFoundExceptionIsThrown() {
        // Arrange
        when(taskRepository.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(42L));
    }

    @Test
    void When_AllTasksAreRequested_Then_AllTasksAreReturned() {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", "some_description");

        final var taskA = new Task("some_name", "some_description", deadline, category);
        taskA.setId(42);

        final var taskB = new Task("some_different_name", "some_different_description", deadline, category);
        taskB.setId(73);

        when(taskRepository.findAll()).thenReturn(Arrays.asList(taskA, taskB));

        // Act
        final var tasks = taskService.getTasks();

        // Assert
        assertEquals(tasks.size(), 2);
        assertTrue(tasks.contains(taskA));
        assertTrue(tasks.contains(taskB));
    }

    @Test
    void When_TaskIsUpdated_Then_TaskRepositorySaveIsCalled() throws TaskNotFoundException {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", "some_description");
        final var taskWithUpdates = new Task("some_new_name", "some_new_description", deadline, category);

        when(taskRepository.findById(42L)).thenReturn(Optional.of(taskWithUpdates));

        // Act
        taskService.updateTask(42L, taskWithUpdates);

        // Assert
        verify(taskRepository, times(1)).save(taskWithUpdates);
    }

    @Test
    void When_TaskIsUpdated_Then_UpdatedTaskIsReturned() throws TaskNotFoundException {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var category = new TaskCategory("some_name", "some_description");
        final var task = new Task("some_name", "some_description", deadline, category);
        task.setId(42);

        final var updatedDeadline = LocalDateTime.now().plusHours(3);
        final var updatedCategory = new TaskCategory("some_new_name", "some_new_description");
        final var taskWithUpdates = new Task("some_new_name", "some_new_description", updatedDeadline, updatedCategory);

        when(taskRepository.findById(42L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        // Act
        final var updatedTask = taskService.updateTask(42L, taskWithUpdates);

        // Assert
        assertEquals(updatedTask.getId(), 42);
        assertEquals(updatedTask.getName(), "some_new_name");
        assertEquals(updatedTask.getDescription(), "some_new_description");
        assertEquals(updatedTask.getDeadline(), updatedDeadline);
        assertEquals(updatedTask.getCategory(), updatedCategory);
    }

    @Test
    void When_TaskIsDeleted_Then_TaskRepositoryDeleteByIdIsCalled() throws TaskNotFoundException {
        // Arrange
        when(taskRepository.existsById(42L)).thenReturn(true);

        // Act
        taskService.deleteTask(42L);

        // Assert
        verify(taskRepository, times(1)).deleteById(42L);
    }

    @Test
    void When_TaskDeletedDoesNotExist_Then_TaskNotFoundExceptionIsThrown() {
        // Arrange
        when(taskRepository.existsById(42L)).thenReturn(false);

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(42L));
    }
}
