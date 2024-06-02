package com.example.demo.UserControllerIntegrationTests;


import com.example.demo.models.User;
import com.example.demo.service.UserService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private UserService userService;

	private Faker faker = new Faker();

	private User getUser() {
		User user = new User();
		user.setUsername(faker.name().username());
		user.setPassword(faker.internet().password());
		user.setId(UUID.randomUUID());
		user.setEmail(faker.internet().emailAddress());
		return user;
	}

	private User createUser() {
		User user = getUser();
		Mockito.when(userService.saveUser(Mockito.any(User.class))).thenReturn(user);
		return user;
	}

	@Test
	public void testRegisterUser() {
		User user = getUser();
		Mockito.when(userService.saveUser(Mockito.any(User.class))).thenReturn(user);

		ResponseEntity<User> response = restTemplate.postForEntity("/api/users/register", user, User.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getUsername()).isEqualTo(user.getUsername());
		assertThat(response.getBody().getEmail()).isEqualTo(user.getEmail());
		assertThat(response.getBody().getPassword()).isEqualTo(user.getPassword());
		assertThat(response.getBody().getId()).isEqualTo(user.getId());
	}

	@Test
	public void testGetByIdUser() {
		User user = createUser();
		Mockito.when(userService.getById(Mockito.any(UUID.class))).thenReturn(user);

		String url = String.format("/api/users/getById/%s", user.getId());
		ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getUsername()).isEqualTo(user.getUsername());
		assertThat(response.getBody().getEmail()).isEqualTo(user.getEmail());
		assertThat(response.getBody().getPassword()).isEqualTo(user.getPassword());
		assertThat(response.getBody().getId()).isEqualTo(user.getId());
	}

	@Test
	public void testGetByUsername() {
		User user = createUser();
		Mockito.when(userService.getByUsername(Mockito.any(String.class))).thenReturn(user);

		String url = String.format("/api/users/getByUsername/%s", user.getUsername());
		ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getUsername()).isEqualTo(user.getUsername());
		assertThat(response.getBody().getEmail()).isEqualTo(user.getEmail());
		assertThat(response.getBody().getPassword()).isEqualTo(user.getPassword());
		assertThat(response.getBody().getId()).isEqualTo(user.getId());
	}

	@Test
	public void testGetUserByEmail() {
		User user = createUser();
		Mockito.when(userService.getByEmail(user.getEmail())).thenReturn(user);

		String url = String.format("/api/users/getByEmail/%s", user.getEmail());
		ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getUsername()).isEqualTo(user.getUsername());
		assertThat(response.getBody().getEmail()).isEqualTo(user.getEmail());
		assertThat(response.getBody().getPassword()).isEqualTo(user.getPassword());
		assertThat(response.getBody().getId()).isEqualTo(user.getId());
	}
}