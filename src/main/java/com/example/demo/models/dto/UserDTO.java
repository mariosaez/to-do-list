package com.example.demo.models.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    private List<TaskDTO> tasks;
}


