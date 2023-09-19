package ch.cern.todo;

import ch.cern.todo.api.models.TaskCategoryRequestModel;
import ch.cern.todo.api.models.TaskCategoryResponseModel;
import ch.cern.todo.api.models.TaskRequestModel;
import ch.cern.todo.api.models.TaskResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoApplicationTests {

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void When_TaskRequestIsPosted_Then_TaskResponseIsReturned() {
        // Arrange
        final var tasksCategoriesResourceLocation = "http://localhost:" + port + "/api/tasks/categories/";
        final var tasksResourceLocation = "http://localhost:" + port + "/api/tasks/";
        final var taskCategoryRequest = new TaskCategoryRequestModel("some_category_name", "some_category_description");
        final var deadline = LocalDateTime.now();
        final var taskRequest = new TaskRequestModel("some_name", "some_description", deadline, taskCategoryRequest);

        final var taskCategoryResponseEntity = restTemplate.postForEntity(tasksCategoriesResourceLocation, taskCategoryRequest, TaskCategoryResponseModel.class);
        final var taskCategoryId = Objects.requireNonNull(taskCategoryResponseEntity.getBody()).getId();

        // Act
        var responseEntity = restTemplate.postForEntity(tasksResourceLocation, taskRequest, TaskResponseModel.class);

        // Assert
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(responseEntity.hasBody());

        final var response = Objects.requireNonNull(responseEntity.getBody());
        assertTrue(response.getId() != 0);
        assertEquals(response.getName(), "some_name");
        assertEquals(response.getDescription(), "some_description");
        assertEquals(response.getDeadline(), deadline);
        assertEquals(response.getCategory().getId(), taskCategoryId);
        assertEquals(response.getCategory().getName(), "some_category_name");
        assertEquals(response.getCategory().getDescription(), "some_category_description");
    }

    @Test
    void When_DeleteTaskCategoryIsCalledAndTaskCategoryIsStillInUseByTask_Then_ExceptionIsThrow() {
        // TODO: Implement
    }

    // TODO: Continue adding integration tests
}
