package com.test.api;

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

import java.sql.Date;

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
		String username = "Michel";
		String birthdate = "2000-01-01";
		String country = "France";
		String phone = "1234567890";
		char gender = 'M';

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.post("/createuser")
						.param("username", username)
						.param("birthdate", birthdate)
						.param("country", country)
						.param("phone", phone)
						.param("gender", Character.toString(gender))
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andReturn().getResponse();

		assertEquals(201, response.getStatus());
	}

	@Test
	void testCreateUserService() {
		userService.createUser("Michel",
				Date.valueOf("2000-01-01"),
				"France",
				"1234567890",
				'M');
		User userResponse = userService.getUserById(1);
		assertEquals("Michel", userResponse.getUsername());
		assertEquals("2000-01-01", userResponse.getBirthdate().toString());
		assertEquals("France", userResponse.getCountry());
		assertEquals("1234567890", userResponse.getPhone());
		assertEquals('M', userResponse.getGender());
	}

	@Test
	void testCreateUserWrongDateFormat() throws Exception {
		String username = "Michel";
		String birthdate = "01-01-2000";
		String country = "France";
		String phone = "1234567890";
		char gender = 'M';

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.post("/createuser")
						.param("username", username)
						.param("birthdate", birthdate)
						.param("country", country)
						.param("phone", phone)
						.param("gender", Character.toString(gender))
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andReturn().getResponse();

		String expectedResponse = "Information is wrong or not in the correct format (use yyyy-MM-dd for birthdate)";
		assertEquals(400, response.getStatus());
		assertEquals(expectedResponse, response.getContentAsString());
	}

	@Test
	void testCreateUserNotFrance() throws Exception {
		String username = "Michel";
		String birthdate = "2000-01-01";
		String country = "Spain";
		String phone = "1234567890";
		char gender = 'M';

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.post("/createuser")
						.param("username", username)
						.param("birthdate", birthdate)
						.param("country", country)
						.param("phone", phone)
						.param("gender", Character.toString(gender))
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andReturn().getResponse();

		String expectedResponse = "User creation is allowed only for adults from France.";
		assertEquals(400, response.getStatus());
		assertEquals(expectedResponse, response.getContentAsString());
	}

	@Test
	void testCreateUserNotAdult() throws Exception {
		String username = "Michel";
		String birthdate = "2010-01-01";
		String country = "France";
		String phone = "1234567890";
		char gender = 'M';

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.post("/createuser")
						.param("username", username)
						.param("birthdate", birthdate)
						.param("country", country)
						.param("phone", phone)
						.param("gender", Character.toString(gender))
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andReturn().getResponse();

		status().isBadRequest();
		String expectedResponse = "User creation is allowed only for adults from France.";
		assertEquals(expectedResponse, response.getContentAsString());
	}

	@Test
	void testCreateUserNoPhone() throws Exception {
		String username = "Michel";
		String birthdate = "2000-01-01";
		String country = "France";
		String phone = "";
		char gender = 'M';

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.post("/createuser")
						.param("username", username)
						.param("birthdate", birthdate)
						.param("country", country)
						.param("phone", phone)
						.param("gender", Character.toString(gender))
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andReturn().getResponse();

		assertEquals(201, response.getStatus());
	}

	@Test
	void testCreateUserNoGender() throws Exception {
		String username = "Michel";
		String birthdate = "2000-01-01";
		String country = "France";
		String phone = "123456789";

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.post("/createuser")
						.param("username", username)
						.param("birthdate", birthdate)
						.param("country", country)
						.param("phone", phone)
						.param("gender", "")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andReturn().getResponse();

		assertEquals(201, response.getStatus());
	}

	@Test
	void testGetUser() throws Exception {
		userService.createUser("Michel",
				Date.valueOf("2000-01-01"),
				"France",
				"1234567890",
				'M');

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.get("/getuser")
						.param("id", String.valueOf(1))
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andReturn().getResponse();

		assertEquals(200, response.getStatus());
	}

	@Test
	void testGetUserNonExistant() throws Exception {
		userService.createUser("Michel",
				Date.valueOf("2000-01-01"),
				"France",
				"1234567890",
				'M');

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.get("/getuser")
						.param("id", String.valueOf(99))
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andReturn().getResponse();

		assertEquals(404, response.getStatus());
	}

	@Test
	void testGetUserWrongParameter() throws Exception {
		userService.createUser("Michel",
				Date.valueOf("2000-01-01"),
				"France",
				"1234567890",
				'M');

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
						.get("/getuser")
						.param("id", "")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andReturn().getResponse();

		assertEquals(400, response.getStatus());
	}
}
