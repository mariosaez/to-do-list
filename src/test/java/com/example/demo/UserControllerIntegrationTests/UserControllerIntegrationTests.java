package com.example.demo.UserControllerIntegrationTests;


import com.example.demo.config.PagedResponse;
import com.example.demo.models.User;
import com.example.demo.models.dto.UserDTO;
import com.example.demo.models.dto.UserRegisterDTO;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebTestClient webTestClient;

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

	@Test
	public void testRegisterUser() {
		UserDTO user = getUser();
		Mockito.when(userService.saveUser(Mockito.any(UserRegisterDTO.class))).thenReturn(user);

		ResponseEntity<User> response = restTemplate.postForEntity("/api/users/register", user, User.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getUsername()).isEqualTo(user.getUsername());
		assertThat(response.getBody().getEmail()).isEqualTo(user.getEmail());
		assertThat(response.getBody().getPassword()).isEqualTo(user.getPassword());
		assertThat(response.getBody().getId()).isEqualTo(user.getId());
	}

	@Test
	public void testLoginUser() {
		UserDTO user = getUser();
		Mockito.when(userService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(user);

		String url = String.format("/api/users/login?username=%s&password=%s", user.getUsername(), user.getPassword());

		ResponseEntity<UserDTO> response = restTemplate.postForEntity(url, user, UserDTO.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getUsername()).isEqualTo(user.getUsername());
		assertThat(response.getBody().getEmail()).isEqualTo(user.getEmail());
		assertThat(response.getBody().getPassword()).isEqualTo(user.getPassword());
		assertThat(response.getBody().getId()).isEqualTo(user.getId());
	}

	@Test
	public void testGetByIdUser() {
		UserDTO user = getUser();
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
		UserDTO user = getUser();
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
		UserDTO user = getUser();
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

	@Test
	public void testGetAllUsers() {
		List<UserDTO> userList = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			userList.add(getUser());
		}
		Mockito.when(userService.findAll()).thenReturn(userList);

		ResponseEntity<List> response = restTemplate.getForEntity("/api/users/getAll", List.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().size()).isEqualTo(5);
	}

	@Test
	public void testGetAllUsersPaginated() {
		Pageable pageable = PageRequest.of(0,100);
		List<UserDTO> userList = new ArrayList<>();
		userList.add(getUser());
		Page<UserDTO> usersResult = new PageImpl<>(userList, pageable, userList.size());
		Mockito.when(userService.findAllPaginated(pageable)).thenReturn(usersResult);

		ResponseEntity<PagedResponse> response = restTemplate.exchange(
				"/api/users/getAllPaginated?page=0&size=100",
				HttpMethod.GET,
				null,
				PagedResponse.class
		);

		assertNotNull(response);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getContent()).isNotEmpty();
	}

	@Test
	public void testUpdateUser() {
		UserDTO user = getUser();
		Mockito.when(userService.updateUser(any())).thenReturn(user);

		user.setEmail("nuevo@gmail.com");
		user.setUsername("newUsername");

		String url = String.format("/api/users/updateUser", user);
		HttpEntity<UserDTO> request = new HttpEntity<>(user);

		ResponseEntity<UserDTO> response = restTemplate.exchange(url, HttpMethod.PUT, request, UserDTO.class);

		assertNotNull(response);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertEquals(response.getBody().getEmail(), user.getEmail());
		assertEquals(response.getBody().getUsername(), user.getUsername());
	}

	@Test
	public void testUpdateUsersList() {
		List<UserDTO> userList = new ArrayList<>();
		UserDTO user = getUser();
		userList.add(user);
		Mockito.when(userService.updateUserList(any())).thenReturn(userList);

		user.setEmail("nuevo@gmail.com");
		user.setUsername("newUsername");

		String url = String.format("/api/users/updateUserList", userList);
		HttpEntity<List> request = new HttpEntity<>(userList);

		ResponseEntity<List<User>> response = restTemplate.exchange(
				url,
				HttpMethod.PUT,
				request,
				new ParameterizedTypeReference<>() {}
		);

		assertNotNull(response);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertEquals(response.getBody().get(0), user);
	}

	@Test
	public void testDeleteUser() {
		UserDTO user = getUser();
		Mockito.when(userService.deleteUser(user.getId())).thenReturn(user);

		webTestClient.method(HttpMethod.DELETE)
				.uri("/api/users/deleteUser")
				.bodyValue(user.getId())
				.exchange()
				.expectStatus().isOk()
				.expectBody(UserDTO.class)
				.isEqualTo(user);

		verify(userService).deleteUser(user.getId());
	}
}
