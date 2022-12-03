package nl.tudelft.sem.project.users.controllers;

import nl.tudelft.sem.project.entities.users.UserDTO;
import nl.tudelft.sem.project.users.database.entities.User;
import nl.tudelft.sem.project.users.database.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DefaultController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/hello")
    public ResponseEntity<String> helloWorld(@RequestBody UserDTO userDTO) {
        var user = userRepository.save(new User(userDTO));
        System.out.println(user.toString());
        return ResponseEntity.ok("Hello World");
    }

}
