package com.example.demo.UserServiceTests;

import com.example.demo.Utils.CustomExceptions;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.UserService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceExceptionTests {

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
    public void testGetByIdUserNotFound() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(CustomExceptions.UserNotFoundException.class, () -> {
           userService.getById(UUID.randomUUID());
        });
    }

    @Test
    public void testGetByUsernameUserNotFound() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        assertThrows(CustomExceptions.UserNotFoundException.class, () -> {
            userService.getByUsername("falseUser");
        });
    }

    @Test
    public void testGetByEmailUserNotFound() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        assertThrows(CustomExceptions.UserNotFoundException.class, () -> {
            userService.getByEmail("falseMail@gmail.com");
        });
    }
}
