package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
public class Task {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    @Column(nullable = false)
    private State state = State.CREATED;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}

