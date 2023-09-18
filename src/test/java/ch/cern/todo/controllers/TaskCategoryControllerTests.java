package ch.cern.todo.controllers;

import ch.cern.todo.api.controllers.TaskCategoryController;
import ch.cern.todo.api.models.TaskCategoryRequestModel;
import ch.cern.todo.api.models.TaskCategoryResponseModel;
import ch.cern.todo.core.TaskCategory;
import ch.cern.todo.mappers.TaskCategoryMapper;
import ch.cern.todo.services.TaskCategoryService;
import ch.cern.todo.services.exceptions.DuplicateTaskCategoryNameException;
import ch.cern.todo.services.exceptions.TaskCategoryNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskCategoryControllerTests {

    @InjectMocks
    private TaskCategoryController taskCategoryController;

    @Spy
    private TaskCategoryMapper taskCategoryMapper;

    @Spy
    private TaskCategoryService taskCategoryService;

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
    void When_CreateTaskCategoryIsCalled_Then_ResponseIsReturned() throws DuplicateTaskCategoryNameException {
        // Arrange
        final var taskCategoryRequest = new TaskCategoryRequestModel("some_name", "some_description");
        final var taskCategory = new TaskCategory("some_name", "some_description");
        final var createdTaskCategory = new TaskCategory("some_name", "some_description");
        createdTaskCategory.setId(42);
        final var taskCategoryResponse = new TaskCategoryResponseModel(42, "some_name", "some_description");

        when(taskCategoryMapper.mapToTaskCategory(taskCategoryRequest)).thenReturn(taskCategory);
        when(taskCategoryService.createTaskCategory(taskCategory)).thenReturn(createdTaskCategory);
        when(taskCategoryMapper.mapToTaskCategoryResponseModel(createdTaskCategory)).thenReturn(taskCategoryResponse);

        // Act
        final var response = taskCategoryController.createTaskCategory(taskCategoryRequest);

        // Assert
        assertEquals(response.getId(), taskCategoryResponse.getId());
        assertEquals(response.getName(), taskCategoryResponse.getName());
        assertEquals(response.getDescription(), taskCategoryResponse.getDescription());
    }

    @Test
    void When_CreateTaskCategoryIsCalled_Then_CreateTaskCategoryIsCalledOnce() throws DuplicateTaskCategoryNameException {
        // Arrange
        final var taskCategoryRequest = new TaskCategoryRequestModel("some_name", "some_description");
        final var taskCategory = new TaskCategory("some_name", "some_description");

        when(taskCategoryMapper.mapToTaskCategory(taskCategoryRequest)).thenReturn(taskCategory);

        // Act
        taskCategoryController.createTaskCategory(taskCategoryRequest);

        // Assert
        verify(taskCategoryService, times(1)).createTaskCategory(taskCategory);
    }

    @Test
    void When_GetTaskCategoryIsCalled_Then_ResponseIsReturned() throws TaskCategoryNotFoundException {
        // Arrange
        final var taskCategory = new TaskCategory("some_name", "some_description");
        taskCategory.setId(42);
        final var taskCategoryResponse = new TaskCategoryResponseModel(42, "some_name", "some_description");

        when(taskCategoryService.getTaskCategoryById(42L)).thenReturn(taskCategory);
        when(taskCategoryMapper.mapToTaskCategoryResponseModel(taskCategory)).thenReturn(taskCategoryResponse);

        // Act
        final var response = taskCategoryController.getTaskCategory(42L);

        // Assert
        assertEquals(response, taskCategoryResponse);
    }

    @Test
    void When_GetTaskCategoriesIsCalled_Then_AllTaskCategoriesAreReturned() {
        // Arrange
        final var taskCategoryA = new TaskCategory("some_name", "some_description");
        taskCategoryA.setId(42);

        final var taskCategoryB = new TaskCategory("some_other_name", "some_other_description");
        taskCategoryB.setId(73);

        final var taskCategoryResponseA = new TaskCategoryResponseModel(42, "some_name", "some_description");
        final var taskCategoryResponseB = new TaskCategoryResponseModel(73, "some_other_name", "some_other_description");

        when(taskCategoryService.getTaskCategories()).thenReturn(List.of(taskCategoryA, taskCategoryB));
        when(taskCategoryMapper.mapToTaskCategoryResponseModel(taskCategoryA)).thenReturn(taskCategoryResponseA);
        when(taskCategoryMapper.mapToTaskCategoryResponseModel(taskCategoryB)).thenReturn(taskCategoryResponseB);

        // Act
        final var response = taskCategoryController.getTaskCategories();

        // Assert
        assertEquals(response.size(), 2);
        assertTrue(response.contains(taskCategoryResponseA));
        assertTrue(response.contains(taskCategoryResponseB));
    }

    @Test
    void When_UpdateTaskCategoryIsCalled_Then_UpdatedTaskCategoryResponseIsReturned() throws TaskCategoryNotFoundException {
        // Arrange
        final var taskCategoryRequest = new TaskCategoryRequestModel("some_new_name", "some_new_description");
        final var taskCategory = new TaskCategory("some_new_name", "some_new_description");
        taskCategory.setId(42);
        final var taskCategoryResponse = new TaskCategoryResponseModel(42, "some_new_name", "some_new_description");

        when(taskCategoryMapper.mapToTaskCategory(taskCategoryRequest)).thenReturn(taskCategory);
        when(taskCategoryService.updateTaskCategory(42L, taskCategory)).thenReturn(taskCategory);
        when(taskCategoryMapper.mapToTaskCategoryResponseModel(taskCategory)).thenReturn(taskCategoryResponse);

        // Act
        final var response = taskCategoryController.updateTaskCategory(42L, taskCategoryRequest);

        // Assert
        assertEquals(response, taskCategoryResponse);
    }

    @Test
    void When_DeleteTaskCategoryIsCalled_Then_TaskServiceDeleteTaskCategoryIsCalled() throws TaskCategoryNotFoundException {
        // Act
        taskCategoryController.deleteTaskCategory(42L);

        // Assert
        verify(taskCategoryService, times(1)).deleteTaskCategory(42L);
    }
}
