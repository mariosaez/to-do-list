package com.example.demo.service;

import com.example.demo.Utils.CustomExceptions;
import com.example.demo.Utils.DataConverter;
import com.example.demo.models.User;
import com.example.demo.models.dto.UserDTO;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserDTO saveUser(UserDTO user) {
        User userToSave = DataConverter.toUser(user);
        User userSaved = userRepository.save(userToSave);
        return DataConverter.toUserDTO(userSaved);
    }

    public UserDTO login(String username, String password) {
        User userFound = userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException("User not found"));
        return DataConverter.toUserDTO(userFound);
    }

    @Transactional
    public UserDTO getById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException("User not found with id: " + id));
        return DataConverter.toUserDTO(user);
    }

    public UserDTO getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException("User not found with username: " + username));
        return DataConverter.toUserDTO(user);
    }

    public UserDTO getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException("User not found with email: " + email));
        return DataConverter.toUserDTO(user);
    }

    public List<UserDTO> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(DataConverter::toUserDTO)
                .collect(Collectors.toList());
    }

    public Page<UserDTO> findAllPaginated(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(DataConverter::toUserDTO);
    }

    @Transactional
    public UserDTO updateUser(UserDTO user) {
        User userToUpdate = DataConverter.toUser(user);
        getById(userToUpdate.getId());
        User updatedUser = userRepository.save(userToUpdate);
        return DataConverter.toUserDTO(updatedUser);
    }

    @Transactional
    public List<UserDTO> updateUserList(List<UserDTO> usersToUpdate) {
        List<UserDTO> updatedUsersDTO = new ArrayList<>();
        usersToUpdate.forEach(user ->
                updatedUsersDTO.add(updateUser(user))
        );
        return updatedUsersDTO;
    }

    @Transactional
    public UserDTO deleteUser(UUID id) {
        User userToDelete = userRepository.getById(id);
        userRepository.deleteById(userToDelete.getId());
        return DataConverter.toUserDTO(userToDelete);
    }
}
