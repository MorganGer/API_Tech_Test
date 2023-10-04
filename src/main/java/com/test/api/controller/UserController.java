package com.test.api.controller;

import com.test.api.model.User;
import com.test.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/getuser")
    public ResponseEntity<User> getUser(@RequestParam Integer id) {
        if(id != null){
            User user = userService.getUserById(id);

            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); //Erreur 404 si le User n'existe pas
            }

            return new ResponseEntity<>(user, HttpStatus.OK); //Code 200 si le User est bien retourné
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/createuser", consumes = {"application/json"})
    public ResponseEntity<String> createUser(@RequestBody User user) {

        String regexPattern = "\\d{4}-\\d{2}-\\d{2}";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(String.valueOf(user.getBirthdate()));

        if(!user.getUsername().isEmpty() && !user.getCountry().isEmpty() && matcher.matches()){ //Si le username et le country sont renseignés et que la birthdate est au bon format
            try {
                Calendar calBirthdate = Calendar.getInstance();
                calBirthdate.setTime(Date.valueOf(user.getBirthdate()));

                Calendar calNow = Calendar.getInstance();
                calNow.add(Calendar.YEAR, -18); //On enlève 18 ans pour vérifier que le User est adulte

                if ("France".equalsIgnoreCase(user.getCountry()) && calBirthdate.before(calNow)) { //Si User Francais et Adulte, on le crée
                    userService.createUser(user);
                    return new ResponseEntity<>("New user created", HttpStatus.CREATED);
                } else { //Sinon, on renvoie une erreur HTTP 400
                    return new ResponseEntity<>("User creation is allowed only for adults from France.", HttpStatus.BAD_REQUEST);

                }
            } catch (Exception exception) { //En cas d'erreur de traitement, erreur serveur interne 500
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Information is wrong or not in the correct format (use yyyy-MM-dd for birthdate)", HttpStatus.BAD_REQUEST);
        }

    }
}
