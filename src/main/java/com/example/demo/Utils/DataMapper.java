package com.example.demo.Utils;

import com.example.demo.models.Task;
import com.example.demo.models.User;
import com.example.demo.models.dto.TaskDTO;
import com.example.demo.models.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DataMapper {
    UserDTO UserToDTO(User user);
    User UserFromDTO(UserDTO userDTO);
    TaskDTO TaskToDTO(Task task);
    Task TaskFromDTO(TaskDTO taskDTO);
}
