package com.test.api.service;

import com.test.api.model.User;
import com.test.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User getUserById(int id) {
        Optional<User> userOptional = userRepository.findById(id);

        return userOptional.orElse(null); //Si aucun User trouvé, on retourne null pour éviter une erreur
    }

    public void createUser(String username, Date birthdate, String country, String phone, Character gender) {
        User user = new User(username, birthdate, country, phone, gender);
        userRepository.save(user);
    }
}
