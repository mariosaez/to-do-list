package com.example.demo.TaskControllerIntegrationTests;

import com.example.demo.Utils.CustomExceptions;
import com.example.demo.Utils.GlobalExceptionHandler;
import com.example.demo.models.dto.StateDTO;
import com.example.demo.models.dto.TaskDTO;
import com.example.demo.service.TaskService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerExceptionTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private TaskService taskService;

    private Faker faker = new Faker();

    private TaskDTO getTask() {
        TaskDTO task = new TaskDTO();
        task.setId(UUID.randomUUID());
        task.setTitle(faker.lorem().sentence());
        task.setContent(faker.lorem().paragraph());
        task.setState(StateDTO.CREATED);
        return task;
    }

    private TaskDTO createTask() {
        TaskDTO task = getTask();
        Mockito.when(taskService.saveTask(Mockito.any(TaskDTO.class))).thenReturn(task);
        return task;
    }

    @Test
    public void testGetByIdTaskNotFound() {
        UUID id = UUID.randomUUID();
        Mockito.when(taskService.getById(id)).thenThrow(new CustomExceptions.UserNotFoundException("Task not found with id: " + id));

        String url = String.format("/api/tasks/getTaskById/%s", id);
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = restTemplate.getForEntity(url, GlobalExceptionHandler.ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Task not found with id: " + id);
    }

    @Test
    public void testGetByTitleTaskNotFound() {
        String title = faker.lorem().sentence();
        Mockito.when(taskService.getByTitle(title)).thenThrow(new CustomExceptions.UserNotFoundException("Task not found with title: " + title));

        String url = String.format("/api/tasks/getTaskByTitle/%s", title);
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = restTemplate.getForEntity(url, GlobalExceptionHandler.ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Task not found with title: " + title);
    }

    @Test
    public void testUpdateTaskNotFound() {
        TaskDTO task = createTask();
        Mockito.when(taskService.updateTask(task)).thenThrow(new CustomExceptions.UserNotFoundException("Task not found with id: " + task.getId()));

        String url = String.format("/api/tasks/updateTask", task);
        HttpEntity<TaskDTO> request = new HttpEntity<>(task);

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = restTemplate.exchange(url, HttpMethod.PUT, request, GlobalExceptionHandler.ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Task not found with id: " + task.getId());
    }

    @Test
    public void testDeleteTaskNotFound() {
        UUID id = UUID.randomUUID();
        Mockito.when(taskService.deleteTask(id)).thenThrow(new CustomExceptions.UserNotFoundException("Task not found with id: " + id));

        String url = String.format("/api/tasks/deleteTask");
        HttpEntity<UUID> request = new HttpEntity<>(id);

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = restTemplate.exchange(url, HttpMethod.DELETE, request, GlobalExceptionHandler.ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Task not found with id: " + id);
    }
}
