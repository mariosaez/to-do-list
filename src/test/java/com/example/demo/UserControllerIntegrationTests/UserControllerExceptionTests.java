package com.example.demo.UserControllerIntegrationTests;

import com.example.demo.Utils.CustomExceptions;
import com.example.demo.Utils.GlobalExceptionHandler;
import com.example.demo.models.dto.UserDTO;
import com.example.demo.service.UserService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerExceptionTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private UserService userService;

    private Faker faker = new Faker();

    private UserDTO getUser() {
        UserDTO user = new UserDTO();
        user.setUsername(faker.name().username());
        user.setPassword(faker.internet().password());
        user.setId(UUID.randomUUID());
        user.setEmail(faker.internet().emailAddress());
        return user;
    }

    private UserDTO createUser() {
        UserDTO user = getUser();
        Mockito.when(userService.saveUser(Mockito.any(UserDTO.class))).thenReturn(user);
        return user;
    }

    @Test
    public void testGetByIdUserNotFound() {
        UUID id = UUID.randomUUID();
        Mockito.when(userService.getById(id)).thenThrow(new CustomExceptions.UserNotFoundException("User not found with id: " + id));

        String url = String.format("/api/users/getById/%s", id);
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = restTemplate.getForEntity(url, GlobalExceptionHandler.ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("User not found with id: " + id);
    }

    @Test
    public void testGetByUsernameNotFound() {
        String username = faker.name().username();
        Mockito.when(userService.getByUsername(username)).thenThrow(new CustomExceptions.UserNotFoundException("User not found with username: " + username));

        String url = String.format("/api/users/getByUsername/%s", username);
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = restTemplate.getForEntity(url, GlobalExceptionHandler.ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("User not found with username: " + username);
    }

    @Test
    public void testGetUserByEmailNotFound() {
        UserDTO user = createUser();
        Mockito.when(userService.getByEmail(user.getEmail())).thenThrow(new CustomExceptions.UserNotFoundException("User not found with email: " + user.getEmail()));

        String url = String.format("/api/users/getByEmail/%s", user.getEmail());
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = restTemplate.getForEntity(url, GlobalExceptionHandler.ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("User not found with email: " + user.getEmail());
    }

    @Test
    public void testUpdateUserNotFound() {
        UserDTO user = createUser();
        Mockito.when(userService.updateUser(user)).thenThrow(new CustomExceptions.UserNotFoundException("User not found with id: " + user.getId()));

        String url = String.format("/api/users/updateUser", user);
        HttpEntity<UserDTO> request = new HttpEntity<>(user);

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = restTemplate.exchange(url, HttpMethod.PUT, request, GlobalExceptionHandler.ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("User not found with id: " + user.getId());
    }
}
