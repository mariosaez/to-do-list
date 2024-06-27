package com.example.demo.TaskControllerIntegrationTests;

import com.example.demo.config.PagedResponse;
import com.example.demo.models.dto.StateDTO;
import com.example.demo.models.dto.TaskDTO;
import com.example.demo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerIntegrationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

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

    @Test
    public void testRegisterTask() {
        TaskDTO task = getTask();
        Mockito.when(taskService.saveTask(Mockito.any(TaskDTO.class))).thenReturn(task);

        ResponseEntity<TaskDTO> response = restTemplate.postForEntity("/api/tasks/registerTask", task, TaskDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo(task.getTitle());
        assertThat(response.getBody().getContent()).isEqualTo(task.getContent());
        assertThat(response.getBody().getState()).isEqualTo(task.getState());
        assertThat(response.getBody().getId()).isEqualTo(task.getId());
    }

    @Test
    public void testGetByIdTask() {
        TaskDTO task = getTask();
        Mockito.when(taskService.getById(Mockito.any(UUID.class))).thenReturn(task);

        String url = String.format("/api/tasks/getTaskById/%s", task.getId());
        ResponseEntity<TaskDTO> response = restTemplate.getForEntity(url, TaskDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo(task.getTitle());
        assertThat(response.getBody().getContent()).isEqualTo(task.getContent());
        assertThat(response.getBody().getState()).isEqualTo(task.getState());
        assertThat(response.getBody().getId()).isEqualTo(task.getId());
    }

    @Test
    public void testGetByTitleTask() {
        TaskDTO task = getTask();
        Mockito.when(taskService.getByTitle(Mockito.any(String.class))).thenReturn(task);

        String url = String.format("/api/tasks/getTaskByTitle/%s", task.getTitle());
        ResponseEntity<TaskDTO> response = restTemplate.getForEntity(url, TaskDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo(task.getTitle());
        assertThat(response.getBody().getContent()).isEqualTo(task.getContent());
        assertThat(response.getBody().getState()).isEqualTo(task.getState());
        assertThat(response.getBody().getId()).isEqualTo(task.getId());
    }

    @Test
    public void testGetAllTasks() {
        List<TaskDTO> taskList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            taskList.add(getTask());
        }
        Mockito.when(taskService.findAll()).thenReturn(taskList);

        ResponseEntity<List> response = restTemplate.getForEntity("/api/tasks/getAllTasks", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(5);
    }

    @Test
    public void testGetTasksByUser() {
        List<TaskDTO> taskList = new ArrayList<>();

        UUID id = UUID.randomUUID();

        for (int i = 0; i < 5; i++) {
            taskList.add(getTask());
        }
        Mockito.when(taskService.getTasksByUserId(any())).thenReturn(taskList);

        String url = String.format("/api/tasks/getTasksByUser/%s", id);
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(5);
    }

    @Test
    public void testGetAllTasksPaginated() {
        Pageable pageable = PageRequest.of(0, 100);
        List<TaskDTO> taskList = new ArrayList<>();
        taskList.add(getTask());
        Page<TaskDTO> tasksResult = new PageImpl<>(taskList, pageable, taskList.size());
        Mockito.when(taskService.findAllPaginated(pageable)).thenReturn(tasksResult);

        ResponseEntity<PagedResponse> response = restTemplate.exchange(
                "/api/tasks/getAllTasksPaginated?page=0&size=100",
                HttpMethod.GET,
                null,
                PagedResponse.class
        );

        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getContent()).isNotEmpty();
    }

    @Test
    public void testUpdateTask() {
        TaskDTO task = getTask();
        Mockito.when(taskService.updateTask(any())).thenReturn(task);

        task.setTitle("Updated Title");
        task.setContent("Updated Content");

        String url = String.format("/api/tasks/updateTask", task);
        HttpEntity<TaskDTO> request = new HttpEntity<>(task);

        ResponseEntity<TaskDTO> response = restTemplate.exchange(url, HttpMethod.PUT, request, TaskDTO.class);

        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(response.getBody().getTitle(), task.getTitle());
        assertEquals(response.getBody().getContent(), task.getContent());
    }

    @Test
    public void testDeleteTask() {
        TaskDTO task = getTask();
        Mockito.when(taskService.deleteTask(task.getId())).thenReturn(task);

        webTestClient.method(HttpMethod.DELETE)
                .uri("/api/tasks/deleteTask")
                .bodyValue(task.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskDTO.class)
                .isEqualTo(task);

        verify(taskService).deleteTask(task.getId());
    }
}
