package com.example.demo.controller;

import com.example.demo.config.PagedResponse;
import com.example.demo.models.User;
import com.example.demo.models.dto.UserDTO;
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

    @PostMapping("/registerUser")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO user) {
        UserDTO savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable UUID id) {
        UserDTO getUser = userService.getById(id);
        return new ResponseEntity<>(getUser, HttpStatus.OK);
    }

    @GetMapping("/getUserByUsername/{username}")
    public ResponseEntity<UserDTO> getByusername(@PathVariable String username) {
        UserDTO getUser = userService.getByUsername(username);
        return new ResponseEntity<>(getUser, HttpStatus.OK);
    }

    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<UserDTO> getByEmail(@PathVariable String email) {
        UserDTO getUser = userService.getByEmail(email);
        return new ResponseEntity<>(getUser, HttpStatus.OK);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDTO>> getAll() {
        List users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/getAllUsersPaginated")
    public ResponseEntity<PagedResponse<UserDTO>> getAllPaginated(
            @RequestParam() int page,
            @RequestParam() int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> users = userService.findAllPaginated(pageable);
        PagedResponse<UserDTO> response = new PagedResponse<>(users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<UserDTO> updateUser(
            @RequestBody UserDTO userToUpdate
    ) {
        UserDTO user = userService.updateUser(userToUpdate);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/updateUserList")
    public ResponseEntity<List> updateUserList(
            @RequestBody List<UserDTO> usersToUpdate
    ) {
        List<UserDTO> userUpdated = userService.updateUserList(usersToUpdate);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }

    @DeleteMapping("deleteUser")
    public ResponseEntity<UserDTO> deleteUser(
            @RequestBody UUID id
    ) {
        UserDTO user = userService.deleteUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
