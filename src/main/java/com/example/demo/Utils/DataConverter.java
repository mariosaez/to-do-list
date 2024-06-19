package com.example.demo.Utils;

import com.example.demo.models.State;
import com.example.demo.models.Task;
import com.example.demo.models.User;
import com.example.demo.models.dto.StateDTO;
import com.example.demo.models.dto.TaskDTO;
import com.example.demo.models.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface DataConverter {

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setTasks(convertToTaskDTOList(user.getTasks()));
        return userDTO;
    }

    public static User toUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setTasks(convertToTaskList(userDTO.getTasks(), user));
        return user;
    }

    public static TaskDTO toTaskDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setContent(task.getContent());
        taskDTO.setState(toStateDTO(task.getState()));
        taskDTO.setUserId(task.getUser().getId());
        return taskDTO;
    }

    public static Task toTask(TaskDTO taskDTO, User user) {
        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setTitle(taskDTO.getTitle());
        task.setContent(taskDTO.getContent());
        task.setState(toState(taskDTO.getState()));
        task.setUser(user);
        return task;
    }

    private static List<TaskDTO> convertToTaskDTOList(List<Task> tasks) {
        return tasks.stream()
                .map(DataConverter::toTaskDTO)
                .collect(Collectors.toList());
    }

    private static List<Task> convertToTaskList(List<TaskDTO> taskDTOs, User user) {
        return taskDTOs.stream()
                .map(taskDTO -> toTask(taskDTO, user))
                .collect(Collectors.toList());
    }

    private static StateDTO toStateDTO(State state) {
        return StateDTO.valueOf(state.name());
    }

    private static State toState(StateDTO stateDTO) {
        return State.valueOf(stateDTO.name());
    }

}
