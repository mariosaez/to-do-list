package com.example.demo.service;

import com.example.demo.Utils.CustomExceptions;
import com.example.demo.Utils.DataMapper;
import com.example.demo.models.Task;
import com.example.demo.models.User;
import com.example.demo.models.dto.TaskDTO;
import com.example.demo.models.dto.UserDTO;
import com.example.demo.repositories.TaskRepository;
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
    private DataMapper dataMapper;

    @Transactional
    public TaskDTO saveTask(TaskDTO task) {
        TaskDTO taskDTO = dataMapper.TaskToDTO(taskRepository.save(dataMapper.TaskFromDTO(task)));
        return taskDTO;
    }

    @Transactional
    public TaskDTO getByTitle(String title) {
        Task task = taskRepository.findByTitle(title)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException("Task not found by" + title));
        TaskDTO taskDTO = dataMapper.TaskToDTO(task);
        return taskDTO;
    }

    @Transactional
    public TaskDTO getById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException("Task not found with id: " + id));
        TaskDTO taskDTO = dataMapper.TaskToDTO(task);
        return taskDTO;
    }

    public List<TaskDTO> findAll() {
        List<Task> taskList = taskRepository.findAll();
        List<TaskDTO> taskDTOList = taskList.stream()
                .map(dataMapper::TaskToDTO)
                .collect(Collectors.toList());
        return taskDTOList;
    }

    public Page<TaskDTO> findAllPaginated(Pageable pageable) {
        Page<Task> taskPage = taskRepository.findAll(pageable);
        Page<TaskDTO> taskDTOPage = taskPage.map(dataMapper::TaskToDTO);
        return taskDTOPage;
    }

    @Transactional
    public TaskDTO updateTask(TaskDTO task) {
        Task taskToUpdate = dataMapper.TaskFromDTO(task);
        Task result = taskRepository.save(taskToUpdate);
        TaskDTO taskDTO = dataMapper.TaskToDTO(result);
        return taskDTO;
    }

    @Transactional
    public TaskDTO deleteTask(UUID id) {
        Task taskToDelete = taskRepository.getById(id);
        taskRepository.deleteById(taskToDelete.getId());
        TaskDTO taskDTO = dataMapper.TaskToDTO(taskToDelete);
        return taskDTO;
    }
}
