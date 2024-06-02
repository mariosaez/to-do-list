package com.example.demo.UserServiceTests;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.UserService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private Faker faker = new Faker();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User getUser() {
        User user = new User();
        user.setUsername(faker.name().username());
        user.setPassword(faker.internet().password());
        user.setId(UUID.randomUUID());
        user.setEmail(faker.internet().emailAddress());
        return user;
    }

    @Test
    public void testGetByIdUser() {
        User user = getUser();
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        User foundUser = userService.getById(user.getId());

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    public void testGetByUsernameUser() {
        User user = getUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User foundUser = userService.getByUsername(user.getUsername());

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    public void testGetByEmailUser() {
        User user = getUser();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User foundUser = userService.getByEmail(user.getEmail());

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    public void testGetAllUsers() {
        List<User> userList = new ArrayList<>();

        for (int i = 0; i <= 4; i++) {
            userList.add(getUser());
        }
        when(userRepository.findAll()).thenReturn(userList);

        List foundUsers = userService.findAll();

        assertNotNull(foundUsers);
        assertEquals(foundUsers.size(), 5);
    }
}

