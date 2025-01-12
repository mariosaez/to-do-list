package com.example.demo.service;

import com.example.demo.Utils.CustomExceptions;
import com.example.demo.Utils.DataConverter;
import com.example.demo.models.Task;
import com.example.demo.models.User;
import com.example.demo.models.dto.TaskDTO;
import com.example.demo.repositories.TaskRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public TaskDTO saveTask(TaskDTO task) {
        User user = DataConverter.toUser(userService.getById(task.getUserId()));
        Task taskToSave = DataConverter.toTask(task, user);
        Task savedTask = taskRepository.save(taskToSave);
        return DataConverter.toTaskDTO(savedTask);
    }

    @Transactional
    public TaskDTO getByTitle(String title) {
        Task task = taskRepository.findByTitle(title)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException("Task not found by " + title));
        return DataConverter.toTaskDTO(task);
    }

    @Transactional
    public TaskDTO getById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException("Task not found with id: " + id));
        return DataConverter.toTaskDTO(task);
    }

    @Transactional
    public List<TaskDTO> getTasksByUserId(UUID id) {
        List<Task> taskList = taskRepository.findByUserId(id);
        return taskList.stream()
                .map(DataConverter::toTaskDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> findAll() {
        List<Task> taskList = taskRepository.findAll();
        return taskList.stream()
                .map(DataConverter::toTaskDTO)
                .collect(Collectors.toList());
    }

    public Page<TaskDTO> findAllPaginated(Pageable pageable) {
        Page<Task> taskPage = taskRepository.findAll(pageable);
        return taskPage.map(DataConverter::toTaskDTO);
    }

    @Transactional
    public TaskDTO updateTask(TaskDTO task) {
        User user = DataConverter.toUser(userService.getById(task.getUserId()));
        Task taskToUpdate = DataConverter.toTask(task, user);
        Task updatedTask = taskRepository.save(taskToUpdate);
        return DataConverter.toTaskDTO(updatedTask);
    }

    @Transactional
    public TaskDTO deleteTask(UUID id) {
        Task taskToDelete = taskRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException("Task not found with id: " + id));
        taskRepository.deleteById(taskToDelete.getId());
        return DataConverter.toTaskDTO(taskToDelete);
    }
}
