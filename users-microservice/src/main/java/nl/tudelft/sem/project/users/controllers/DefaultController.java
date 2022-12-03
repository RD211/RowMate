package nl.tudelft.sem.project.users.controllers;

import nl.tudelft.sem.project.entities.users.UserDTO;
import nl.tudelft.sem.project.users.database.entities.User;
import nl.tudelft.sem.project.users.database.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The default controller.
 * Just here to show an example of how we should work.
 */
@RestController
public class DefaultController {

    @Autowired
    transient UserRepository userRepository;

    /**
     * The add user endpoint. It adds a user to the database given the UserDTO.
     *
     * @param userDTO the user dto, the data that will be added to the database.
     * @return the updated dto including the ID.
     */
    @PostMapping("/add_user")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) {
        var user = userRepository.save(new User(userDTO));
        return ResponseEntity.ok(user.toDTO());
    }

    /**
     * The get user endpoint. Gets the userDTO given the username of the user.
     *
     * @param username the username of the user.
     * @return the complete user-dto.
     */
    @GetMapping("/get_user")
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam String username) {
        var user = userRepository.findByUsername(username);
        return user.map(value -> ResponseEntity.ok(value.toDTO()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
