package com.example.demo.TaskServiceTests;

import com.example.demo.Utils.DataConverter;
import com.example.demo.models.Task;
import com.example.demo.models.User;
import com.example.demo.models.dto.StateDTO;
import com.example.demo.models.dto.TaskDTO;
import com.example.demo.repositories.TaskRepository;
import com.example.demo.service.TaskService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TaskServiceTests {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Faker faker = new Faker();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private TaskDTO getTask() {
        TaskDTO task = new TaskDTO();
        task.setId(UUID.randomUUID());
        task.setTitle(faker.lorem().sentence());
        task.setContent(faker.lorem().paragraph());
        task.setState(StateDTO.CREATED);
        task.setUserId(UUID.randomUUID());
        return task;
    }

    @Test
    public void testSaveTask() {
        TaskDTO task = getTask();
        Task newTask = DataConverter.toTask(task, new User());

        when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        TaskDTO savedTask = taskService.saveTask(task);

        assertNotNull(savedTask);
        assertEquals(task.getId(), savedTask.getId());
        assertEquals(task.getTitle(), savedTask.getTitle());
        assertEquals(task.getContent(), savedTask.getContent());
        assertEquals(task.getState(), savedTask.getState());
    }

    @Test
    public void testGetByIdTask() {
        TaskDTO task = getTask();
        Task newTask = DataConverter.toTask(task, new User());

        when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.of(newTask));

        TaskDTO foundTask = taskService.getById(task.getId());

        assertNotNull(foundTask);
        assertEquals(task.getId(), foundTask.getId());
        assertEquals(task.getTitle(), foundTask.getTitle());
        assertEquals(task.getContent(), foundTask.getContent());
        assertEquals(task.getState(), foundTask.getState());
    }

    @Test
    public void testGetByTitleTask() {
        TaskDTO task = getTask();
        Task newTask = DataConverter.toTask(task, new User());

        when(taskRepository.findByTitle(task.getTitle())).thenReturn(Optional.of(newTask));

        TaskDTO foundTask = taskService.getByTitle(task.getTitle());

        assertNotNull(foundTask);
        assertEquals(task.getId(), foundTask.getId());
        assertEquals(task.getTitle(), foundTask.getTitle());
        assertEquals(task.getContent(), foundTask.getContent());
        assertEquals(task.getState(), foundTask.getState());
    }

    @Test
    public void testFindAllTasks() {
        List<TaskDTO> taskList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            taskList.add(getTask());
        }

        List<Task> newTaskList = taskList.stream()
                .map(taskDTO -> DataConverter.toTask(taskDTO, new User()))
                .collect(Collectors.toList());

        when(taskRepository.findAll()).thenReturn(newTaskList);

        List<TaskDTO> foundTasks = taskService.findAll();

        assertNotNull(foundTasks);
        assertEquals(foundTasks.size(), 5);
    }

    @Test
    public void testFindAllPaginated() {
        List<TaskDTO> taskList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            taskList.add(getTask());
        }
        Pageable pageable = PageRequest.of(0, 100);
        Page<TaskDTO> tasksResult = new PageImpl<>(taskList, pageable, taskList.size());

        Page<Task> taskPageToFind = tasksResult.map(taskDTO -> DataConverter.toTask(taskDTO, new User()));

        when(taskRepository.findAll(pageable)).thenReturn(taskPageToFind);

        Page<TaskDTO> tasks = taskService.findAllPaginated(pageable);

        assertNotNull(tasks);
        assertEquals(tasks.getContent().size(), 5);
    }

    @Test
    public void testUpdateTask() {
        TaskDTO task = getTask();
        task.setTitle("Updated Title");
        task.setContent("Updated Content");

        Task newTask = DataConverter.toTask(task, new User());

        when(taskRepository.save(newTask)).thenReturn(newTask);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(newTask));

        TaskDTO response = taskService.updateTask(task);

        assertNotNull(response);
        assertEquals(response.getTitle(), task.getTitle());
        assertEquals(response.getContent(), task.getContent());
        assertEquals(response.getId(), task.getId());
    }

    @Test
    public void testDeleteTask() {
        TaskDTO task = getTask();
        Task newTask = DataConverter.toTask(task, new User());

        when(taskRepository.getById(task.getId())).thenReturn(newTask);
        doNothing().when(taskRepository).deleteById(task.getId());

        taskService.deleteTask(task.getId());

        verify(taskRepository).deleteById(task.getId());
    }
}
