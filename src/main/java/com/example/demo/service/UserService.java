package com.example.demo.service;

import com.example.demo.Utils.CustomExceptions;
import com.example.demo.Utils.DataMapper;
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
    @Autowired
    private DataMapper dataMapper;

    @Transactional
    public UserDTO saveUser(UserDTO user) {
        UserDTO userDTO = dataMapper.toDTO(userRepository.save(dataMapper.fromDTO(user)));
        return userDTO;
    }

    @Transactional
    public UserDTO getById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException("User not found with id: " + id));
        UserDTO userDTO = dataMapper.toDTO(user);
        return userDTO;
    }

    public UserDTO getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException("User not found with username: " + username));
        UserDTO userDTO = dataMapper.toDTO(user);
        return userDTO;
    }

    public UserDTO getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException("User not found with email: " + email));
        UserDTO userDTO = dataMapper.toDTO(user);
        return userDTO;
    }

    public List<UserDTO> findAll() {
        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOList = userList.stream()
                .map(dataMapper::toDTO)
                .collect(Collectors.toList());
        return userDTOList;
    }

    public Page<UserDTO> findAllPaginated(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        Page<UserDTO> userDTOPage = userPage.map(dataMapper::toDTO);
        return userDTOPage;

    }

    @Transactional
    public UserDTO updateUser(UserDTO user) {
        User userToUpdate = dataMapper.fromDTO(user);
        User result = userRepository.save(userToUpdate);
        UserDTO userDTO = dataMapper.toDTO(result);
        return userDTO;
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
        UserDTO userDTO = dataMapper.toDTO(userToDelete);
        return userDTO;
    }
}
