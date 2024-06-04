package com.example.demo.controller;

import com.example.demo.config.PagedResponse;
import com.example.demo.models.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
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

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAll() {
        List users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/getAllPaginated")
    public ResponseEntity<PagedResponse<User>> getAllPaginated(
            @RequestParam() int page,
            @RequestParam() int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.findAllPaginated(pageable);
        PagedResponse<User> response = new PagedResponse<>(users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<User> updateUser(
            @RequestBody User userToUpdate
    ) {
        User user = userService.updateUser(userToUpdate);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/updateUserList")
    public ResponseEntity<List> updateUserList(
            @RequestBody List<User> usersToUpdate
    ) {
        List<User> userUpdated = userService.updateUserList(usersToUpdate);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }

}
