package com.example.demo.models.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserRegisterDTO {
    private UUID id = UUID.randomUUID();
    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
}
