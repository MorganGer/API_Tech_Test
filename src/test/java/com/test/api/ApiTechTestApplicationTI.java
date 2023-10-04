package com.test.api;

import com.test.api.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTechTestApplicationTI {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private TestH2Repository testH2Repository;

    @BeforeAll
    public static void init(){
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp(){
        baseUrl = baseUrl.concat(":").concat(port + "");
    }

    @Test
    void testCreateUser(){
//        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
//        params.add("username", "Michel");
//        params.add("birthdate", "2000-01-01");
//        params.add("country", "France");
//        params.add("phone", "1122334455");
//        params.add("gender", "M");
//
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params);

        User user = new User("Michel",
                "2000-01-01",
                "France",
                "1122334455",
                "M");

        String response = restTemplate.postForObject(baseUrl.concat("/createuser"), user, String.class);
        assert response != null;
        assertEquals("New user created", response);
        assertEquals(1, testH2Repository.findAll().size());
    }

    @Test
    @Sql(statements = "INSERT INTO USERS (username, birthdate, country, phone, gender) " +
            "VALUES ('Michel', '2000-01-01', 'France', '1122334455', 'M')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM USERS WHERE username='Michel'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testGetUser(){
        User response = restTemplate.getForObject(baseUrl.concat("/getuser?id=1"), User.class);
        assert response != null;
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(1, response.getId()),
                () -> assertEquals("Michel", response.getUsername()),
                () -> assertEquals("2000-01-01", String.valueOf(response.getBirthdate())),
                () -> assertEquals("France", response.getCountry()),
                () -> assertEquals("1122334455", response.getPhone()),
                () -> assertEquals("M", response.getGender())
        );
    }

}
