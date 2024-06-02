package com.example.demo.controller;

import com.example.demo.models.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<User> getById(@PathVariable UUID id) {
        User getUser = userService.getById(id);
        return new ResponseEntity<>(getUser, HttpStatus.OK);
    }

    @GetMapping("/getByUsername/{username}")
    public ResponseEntity<User> getByusername(@PathVariable String username) {
        User getUser = userService.getByUsername(username);
        return new ResponseEntity<>(getUser, HttpStatus.OK);
    }

    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable String email) {
        User getUser = userService.getByEmail(email);
        return new ResponseEntity<>(getUser, HttpStatus.OK);
    }
}
