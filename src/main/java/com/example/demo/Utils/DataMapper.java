package com.example.demo.Utils;

import com.example.demo.models.User;
import com.example.demo.models.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DataMapper {
    UserDTO toDTO(User user);
    User fromDTO(UserDTO userDTO);
}
