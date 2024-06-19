package com.example.demo.controller;

import com.example.demo.config.PagedResponse;
import com.example.demo.models.dto.TaskDTO;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/registerTask")
    public ResponseEntity<TaskDTO> registerTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO savedTask = taskService.saveTask(taskDTO);
        return new ResponseEntity<>(savedTask, HttpStatus.OK);
    }

    @GetMapping("/getTaskById/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable UUID id) {
        TaskDTO getTask = taskService.getById(id);
        return new ResponseEntity<>(getTask, HttpStatus.OK);
    }

    @GetMapping("/getTaskByTitle/{title}")
    public ResponseEntity<TaskDTO> getByTitle(@PathVariable String title) {
        TaskDTO getTask = taskService.getByTitle(title);
        return new ResponseEntity<>(getTask, HttpStatus.OK);
    }

    @GetMapping("/getAllTasks")
    public ResponseEntity<List<TaskDTO>> getAll() {
        List tasks = taskService.findAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/getAllTasksPaginated")
    public ResponseEntity<PagedResponse<TaskDTO>> getAllPaginated(
            @RequestParam() int page,
            @RequestParam() int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskDTO> tasks = taskService.findAllPaginated(pageable);
        PagedResponse<TaskDTO> response = new PagedResponse<>(tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/updateTask")
    public ResponseEntity<TaskDTO> updateTask(
            @RequestBody TaskDTO taskToUpdate
    ) {
        TaskDTO task = taskService.updateTask(taskToUpdate);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("deleteTask")
    public ResponseEntity<TaskDTO> deleteTask(
            @RequestBody UUID id
    ) {
        TaskDTO task = taskService.deleteTask(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}
