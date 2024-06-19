package com.example.demo.UserServiceTests;

import com.example.demo.Utils.DataMapper;
import com.example.demo.models.User;
import com.example.demo.models.dto.TaskDTO;
import com.example.demo.models.dto.UserDTO;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.UserService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private DataMapper dataMapper;

    private Faker faker = new Faker();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private UserDTO getUser() {
        UserDTO user = new UserDTO();
        user.setUsername(faker.name().username());
        user.setPassword(faker.internet().password());
        user.setId(UUID.randomUUID());
        user.setEmail(faker.internet().emailAddress());
        return user;
    }

    private TaskDTO getTask() {
        TaskDTO task = new TaskDTO();
        task.setId(UUID.randomUUID());
        task.setTitle("task 1");
        return task;
    }

    @Test
    public void testGetByIdUser() {
        UserDTO user = getUser();
        TaskDTO task = getTask();
        ArrayList<TaskDTO> taskList = new ArrayList<>();
        taskList.add(task);
        task.setUserId(user.getId());
        user.setTasks(taskList);

        User newUser = new User();
        user.setId(user.getId());
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());
        
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(newUser));
        when(dataMapper.UserToDTO(any(User.class))).thenReturn(user);

        UserDTO foundUser = userService.getById(user.getId());

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    public void testGetByUsernameUser() {
        UserDTO user = getUser();

        User newUser = new User();
        user.setId(user.getId());
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(newUser));
        when(dataMapper.UserToDTO(any(User.class))).thenReturn(user);

        UserDTO foundUser = userService.getByUsername(user.getUsername());

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    public void testGetByEmailUser() {
        UserDTO user = getUser();

        User newUser = new User();
        user.setId(user.getId());
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(newUser));
        when(dataMapper.UserToDTO(any(User.class))).thenReturn(user);

        UserDTO foundUser = userService.getByEmail(user.getEmail());

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    public void testGetAllUsers() {
        List<UserDTO> userList = new ArrayList<>();

        for (int i = 0; i <= 4; i++) {
            userList.add(getUser());
        }
        List<User> newUserList = userList.stream()
                        .map(dataMapper::UserFromDTO)
                                .collect(Collectors.toList());

        when(userRepository.findAll()).thenReturn(newUserList);

        List foundUsers = userService.findAll();

        assertNotNull(foundUsers);
        assertEquals(foundUsers.size(), 5);
    }

    @Test
    public void testGetAllPaginated() {
        List<UserDTO> userList = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            userList.add(getUser());
        }
        Pageable pageable = PageRequest.of(0, 100);
        Page<UserDTO> usersResult = new PageImpl<>(userList, pageable, userList.size());

        Page<User> userPageToFind = usersResult.map(dataMapper::UserFromDTO);

        when(userRepository.findAll(pageable)).thenReturn(userPageToFind);

        Page<UserDTO> users = userService.findAllPaginated(pageable);

        assertNotNull(users);
        assertEquals(users.getContent().size(), 5);
    }

    @Test
    public void testGetAllPaginatedEmptyReturnEmptyPage() {
        List<User> userList = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 100);
        Page<User> usersResult = new PageImpl<>(userList, pageable, userList.size());

        when(userRepository.findAll(pageable)).thenReturn(usersResult);

        Page<UserDTO> users = userService.findAllPaginated(pageable);

        assertNotNull(users);
        assertEquals(users.getContent().size(), 0);
    }

    @Test
    public void testUpdateUser() {
        UserDTO user = getUser();
        user.setEmail("nuevo@gmail.com");
        user.setUsername("newUsername");

        User newUser = dataMapper.UserFromDTO(user);

        when(userRepository.save(newUser)).thenReturn(newUser);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(newUser));

        UserDTO response = userService.updateUser(user);

        assertNotNull(response);
        assertEquals(response.getUsername(), user.getUsername());
        assertEquals(response.getEmail(), user.getEmail());
        assertEquals(response.getId(), user.getId());
    }

    @Test
    public void testDeleteUser() {
        UserDTO user = getUser();
        User newUser = dataMapper.UserFromDTO(user);

        when(userRepository.getById(user.getId())).thenReturn(newUser);
        doNothing().when(userRepository).deleteById(user.getId());

        userService.deleteUser(user.getId());

        verify(userRepository).deleteById(user.getId());
    }
}


