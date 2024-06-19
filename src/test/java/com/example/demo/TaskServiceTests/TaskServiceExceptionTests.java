package com.example.demo.TaskServiceTests;

import com.example.demo.Utils.CustomExceptions;
import com.example.demo.models.State;
import com.example.demo.models.Task;
import com.example.demo.models.dto.TaskDTO;
import com.example.demo.repositories.TaskRepository;
import com.example.demo.service.TaskService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TaskServiceExceptionTests {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Faker faker = new Faker();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Task getTask() {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle(faker.lorem().sentence());
        task.setContent(faker.lorem().paragraph());
        task.setState(State.CREATED);
        return task;
    }

    @Test
    public void testGetByIdTaskNotFound() {
        when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(CustomExceptions.UserNotFoundException.class, () -> {
            taskService.getById(UUID.randomUUID());
        });
    }

    @Test
    public void testGetByTitleTaskNotFound() {
        when(taskRepository.findByTitle(any(String.class))).thenReturn(Optional.empty());

        assertThrows(CustomExceptions.UserNotFoundException.class, () -> {
            taskService.getByTitle("nonExistentTitle");
        });
    }

    @Test
    public void testUpdateTaskNotFound() {
        TaskDTO taskDTO = new TaskDTO();
        when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(CustomExceptions.UserNotFoundException.class, () -> {
            taskService.updateTask(taskDTO);
        });
    }

    @Test
    public void testDeleteTaskNotFound() {
        when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(CustomExceptions.UserNotFoundException.class, () -> {
            taskService.deleteTask(UUID.randomUUID());
        });
    }
}
