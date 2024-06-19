package com.example.demo.Utils;

import com.example.demo.models.Task;
import com.example.demo.models.User;
import com.example.demo.models.dto.TaskDTO;
import com.example.demo.models.dto.UserDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DataMapper {
    UserDTO UserToDTO(User user);
    User UserFromDTO(UserDTO userDTO);

    @Mapping(source = "user.id", target = "userId")
    TaskDTO TaskToDTO(Task task);

    @Mapping(target = "user", ignore = true)
    Task TaskFromDTO(TaskDTO taskDTO);

    @AfterMapping
    default void setUserId(@MappingTarget TaskDTO taskDTO, Task task) {
        if (task.getUser() != null) {
            taskDTO.setUserId(task.getUser().getId());
        }
    }
}
