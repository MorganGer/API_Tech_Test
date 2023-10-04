package com.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.api.controller.UserController;
import com.test.api.model.User;
import com.test.api.repository.UserRepository;
import com.test.api.service.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiTechTestApplicationTU {

	@Autowired
	UserService userService;

	@Autowired
	UserController userController;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MockMvc mockMvc;

	@Before("")
	public void setup() {
		userService = mock(UserService.class);
		userController = new UserController(userService);
	}

	@Test
	void testCreateUser() throws Exception {
		User user = new User("Michel",
				"2000-01-01",
				"France",
				"1122334455",
				"M"
		);

		ObjectMapper objectMapper = new ObjectMapper();
		String userJson = objectMapper.writeValueAsString(user);

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.post("/createuser")
						.param("user", userJson)
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andReturn().getResponse();

		assertEquals(201, response.getStatus());
	}

	@Test
	void testCreateUserService() {
		User user = new User("Michel",
				"2000-01-01",
				"France",
				"1122334455",
				"M"
		);

		userService.createUser(user);
		User userResponse = userService.getUserById(1);
		assertEquals("Michel", userResponse.getUsername());
		assertEquals("2000-01-01", userResponse.getBirthdate());
		assertEquals("France", userResponse.getCountry());
		assertEquals("1122334455", userResponse.getPhone());
		assertEquals("M", userResponse.getGender());
	}

	@Test
	void testCreateUserWrongDateFormat() throws Exception {
		User user = new User("Michel",
				"01-01-2000",
				"France",
				"1122334455",
				"M"
		);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String userJson = objectMapper.writeValueAsString(user);

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.post("/createuser")
						.param("user", userJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		String expectedResponse = "Information is wrong or not in the correct format (use yyyy-MM-dd for birthdate)";
		assertEquals(400, response.getStatus());
		assertEquals(expectedResponse, response.getContentAsString());
	}

	@Test
	void testCreateUserNotFrance() throws Exception {
		User user = new User("Michel",
				"2000-01-01",
				"Spain",
				"1122334455",
				"M"
		);

		ObjectMapper objectMapper = new ObjectMapper();
		String userJson = objectMapper.writeValueAsString(user);


		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.post("/createuser")
						.param("user", userJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		String expectedResponse = "User creation is allowed only for adults from France.";
		assertEquals(400, response.getStatus());
		assertEquals(expectedResponse, response.getContentAsString());
	}

	@Test
	void testCreateUserNotAdult() throws Exception {
		User user = new User("Michel",
				"2010-01-01",
				"France",
				"1122334455",
				"M"
		);

		ObjectMapper objectMapper = new ObjectMapper();
		String userJson = objectMapper.writeValueAsString(user);


		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.post("/createuser")
						.param("user", userJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		status().isBadRequest();
		String expectedResponse = "User creation is allowed only for adults from France.";
		assertEquals(expectedResponse, response.getContentAsString());
	}

	@Test
	void testCreateUserNoPhone() throws Exception {
		User user = new User("Michel",
				"2000-01-01",
				"France",
				"",
				"M"
		);

		ObjectMapper objectMapper = new ObjectMapper();
		String userJson = objectMapper.writeValueAsString(user);


		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.post("/createuser")
						.param("user", userJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		assertEquals(201, response.getStatus());
	}

	@Test
	void testCreateUserNoGender() throws Exception {
		User user = new User("Michel",
				"2000-01-01",
				"France",
				"1122334455",
				""
		);

		ObjectMapper objectMapper = new ObjectMapper();
		String userJson = objectMapper.writeValueAsString(user);
		
		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.post("/createuser")
						.param("user", userJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		assertEquals(201, response.getStatus());
	}

	@Test
	void testGetUser() throws Exception {
		User user = new User("Michel",
				"2000-01-01",
				"France",
				"1122334455",
				"M"
		);

		userService.createUser(user);

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.get("/getuser")
						.param("id", String.valueOf(1))
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andReturn().getResponse();

		assertEquals(200, response.getStatus());
	}

	@Test
	void testGetUserNonExistant() throws Exception {
		User user = new User("Michel",
				"2000-01-01",
				"France",
				"1122334455",
				"M"
		);
		userService.createUser(user);

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.get("/getuser")
						.param("id", String.valueOf(99))
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andReturn().getResponse();

		assertEquals(404, response.getStatus());
	}

	@Test
	void testGetUserWrongParameter() throws Exception {
		User user = new User("Michel",
				"2000-01-01",
				"France",
				"1122334455",
				"M"
		);

		userService.createUser(user);

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.get("/getuser")
						.param("id", "")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andReturn().getResponse();

		assertEquals(400, response.getStatus());
	}
}
