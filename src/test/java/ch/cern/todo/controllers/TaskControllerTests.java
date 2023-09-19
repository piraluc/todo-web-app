package ch.cern.todo.controllers;

import ch.cern.todo.api.controllers.TaskController;
import ch.cern.todo.api.models.TaskCategoryRequestModel;
import ch.cern.todo.api.models.TaskCategoryResponseModel;
import ch.cern.todo.api.models.TaskRequestModel;
import ch.cern.todo.api.models.TaskResponseModel;
import ch.cern.todo.core.Task;
import ch.cern.todo.core.TaskCategory;
import ch.cern.todo.mappers.TaskMapper;
import ch.cern.todo.services.TaskService;
import ch.cern.todo.services.exceptions.TaskCategoryNotFoundException;
import ch.cern.todo.services.exceptions.TaskNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskControllerTests {
    @InjectMocks
    private TaskController taskController;

    @Spy
    private TaskMapper taskMapper;

    @Spy
    private TaskService taskService;

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
    void When_CreateTaskIsCalled_Then_ResponseIsReturned() throws TaskCategoryNotFoundException {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var taskCategoryRequest = new TaskCategoryRequestModel("some_category_name", "some_category_description");
        final var taskRequest = new TaskRequestModel("some_name", "some_description", deadline, taskCategoryRequest);
        final var taskCategory = new TaskCategory("some_category_name", "some_description");
        final var task = new Task("some_name", "some_description", deadline, taskCategory);
        final var taskCategoryResponse = new TaskCategoryResponseModel(73, "some_category_name", "some_category_description");
        final var taskResponse = new TaskResponseModel(42L, "some_name", "some_description", deadline, taskCategoryResponse);


        when(taskMapper.mapToTask(taskRequest)).thenReturn(task);
        when(taskService.createTask(task)).thenReturn(task);
        when(taskMapper.mapToTaskResponse(task)).thenReturn(taskResponse);

        // Act
        final var response = taskController.createTask(taskRequest);

        // Assert
        assertEquals(response, taskResponse);
    }

    @Test
    void When_CreateTaskIsCalledAndTaskCategoryDoesNotExist_Then_ResponseStatusExceptionNotFoundIsThrown() throws TaskCategoryNotFoundException {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var taskCategoryRequest = new TaskCategoryRequestModel("some_category_name", "some_category_description");
        final var taskRequest = new TaskRequestModel("some_name", "some_description", deadline, taskCategoryRequest);
        final var taskCategory = new TaskCategory("some_category_name", "some_description");
        final var task = new Task("some_name", "some_description", deadline, taskCategory);


        when(taskMapper.mapToTask(taskRequest)).thenReturn(task);
        when(taskService.createTask(task)).thenThrow(new TaskCategoryNotFoundException());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> taskController.createTask(taskRequest));
    }

    @Test
    void When_CreateTaskIsCalled_Then_TaskServiceCreateTaskIsCalledOnce() throws TaskCategoryNotFoundException {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var taskCategoryRequest = new TaskCategoryRequestModel("some_category_name", "some_category_description");
        final var taskRequest = new TaskRequestModel("some_name", "some_description", deadline, taskCategoryRequest);
        final var taskCategory = new TaskCategory("some_category_name", "some_description");
        final var task = new Task("some_name", "some_description", deadline, taskCategory);

        when(taskMapper.mapToTask(taskRequest)).thenReturn(task);

        // Act
        taskController.createTask(taskRequest);

        // Assert
        verify(taskService, times(1)).createTask(task);
    }

    @Test
    void When_GetTaskIsCalled_Then_ResponseIsReturned() throws TaskNotFoundException {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var taskCategory = new TaskCategory("some_category_name", "some_category_description");
        final var task = new Task("some_name", "some_description", deadline, taskCategory);
        final var taskCategoryResponse = new TaskCategoryResponseModel(73, "some_category_name", "some_category_description");
        final var taskResponse = new TaskResponseModel(42L, "some_name", "some_description", deadline, taskCategoryResponse);

        when(taskService.getTaskById(42L)).thenReturn(task);
        when(taskMapper.mapToTaskResponse(task)).thenReturn(taskResponse);

        // Act
        final var response = taskController.getTask(42L);

        // Assert
        assertEquals(response, taskResponse);
    }

    @Test
    void When_GetTasksIsCalled_Then_AllTasksAreReturned() {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var taskCategory = new TaskCategory("some_category_name", "some_category_description");

        final var taskA = new Task("some_name", "some_description", deadline, taskCategory);
        final var taskB = new Task("some_other_name", "some_other_description", deadline, taskCategory);

        final var taskCategoryResponse = new TaskCategoryResponseModel(73, "some_category_name", "some_category_description");
        final var taskResponseA = new TaskResponseModel(42L, "some_name", "some_description", deadline, taskCategoryResponse);
        final var taskResponseB = new TaskResponseModel(6L, "some_name", "some_description", deadline, taskCategoryResponse);

        when(taskService.getTasks()).thenReturn(List.of(taskA, taskB));
        when(taskMapper.mapToTaskResponse(taskA)).thenReturn(taskResponseA);
        when(taskMapper.mapToTaskResponse(taskB)).thenReturn(taskResponseB);

        // Act
        final var response = taskController.getTasks();

        // Assert
        assertEquals(response.size(), 2);
        assertTrue(response.contains(taskResponseA));
        assertTrue(response.contains(taskResponseB));
    }

    @Test
    void When_UpdateTaskIsCalled_Then_UpdatedTaskIsReturned() throws TaskNotFoundException, TaskCategoryNotFoundException {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var taskCategoryRequest = new TaskCategoryRequestModel("some_category_name", "some_category_description");
        final var taskRequest = new TaskRequestModel("some_name", "some_description", deadline, taskCategoryRequest);
        final var taskCategory = new TaskCategory("some_category_name", "some_category_description");
        final var task = new Task("some_name", "some_description", deadline, taskCategory);
        final var taskCategoryResponse = new TaskCategoryResponseModel(73, "some_category_name", "some_category_description");
        final var taskResponse = new TaskResponseModel(42L, "some_name", "some_description", deadline, taskCategoryResponse);

        when(taskMapper.mapToTask(taskRequest)).thenReturn(task);
        when(taskService.updateTask(42L, task)).thenReturn(task);
        when(taskMapper.mapToTaskResponse(task)).thenReturn(taskResponse);

        // Act
        final var response = taskController.updateTask(42L, taskRequest);

        // Assert
        assertEquals(response, taskResponse);
    }

    @Test
    void When_UpdateTaskIsCalled_Then_TaskServiceUpdateTaskIsCalledOnce() throws TaskNotFoundException, TaskCategoryNotFoundException {
        // Arrange
        final var deadline = LocalDateTime.now();
        final var taskCategoryRequest = new TaskCategoryRequestModel("some_category_name", "some_category_description");
        final var taskRequest = new TaskRequestModel("some_name", "some_description", deadline, taskCategoryRequest);
        final var taskCategory = new TaskCategory("some_category_name", "some_category_description");
        final var task = new Task("some_name", "some_description", deadline, taskCategory);

        when(taskMapper.mapToTask(taskRequest)).thenReturn(task);

        // Act
        taskController.updateTask(42L, taskRequest);

        // Assert
        verify(taskService, times(1)).updateTask(42L, task);
    }

    @Test
    void When_DeleteTaskIsCalled_Then_TaskServiceDeleteTaskIsCalledOnce() throws TaskNotFoundException {
        // Act
        taskController.deleteTask(42L);

        // Assert
        verify(taskService, times(1)).deleteTask(42L);
    }
}
