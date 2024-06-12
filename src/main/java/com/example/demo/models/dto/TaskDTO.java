package com.example.demo.models.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TaskDTO {
    private UUID id;
    private String title;
    private String content;
    private StateDTO state;
    private UUID userId;
}
