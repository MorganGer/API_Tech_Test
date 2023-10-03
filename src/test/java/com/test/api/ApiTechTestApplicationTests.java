package com.test.api;

import com.test.api.model.User;
import com.test.api.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApiTechTestApplicationTests {

	@Autowired
	UserService userService;

	@Test
	void testCreateUser() {
		User user = new User(
				"Michel",
				Date.valueOf("2000-01-01"),
				"France",
				null,
				null
		);
		userService.createUser(user.getUsername(), user.getBirthdate(), user.getCountry(), user.getPhone(), user.getGender());
		assertNotNull(userService.getUserById(1));
	}
}
