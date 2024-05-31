package com.example.demo;

import com.example.demo.models.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private UserService userService;

	@Test
	public void testRegisterUser() {
		User user = new User();
		user.setUsername("testuser");
		user.setPassword("password123");
		user.setEmail("testuser@example.com");

		Mockito.when(userService.saveUser(Mockito.any(User.class))).thenReturn(user);

		ResponseEntity<User> response = restTemplate.postForEntity("/api/users/register", user, User.class);

		assertThat(response.getStatusCode()).isEqualTo(CREATED);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getUsername()).isEqualTo("testuser");

	}

}
