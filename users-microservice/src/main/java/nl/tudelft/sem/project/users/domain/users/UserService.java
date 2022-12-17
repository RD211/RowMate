package nl.tudelft.sem.project.users.domain.users;

import lombok.NonNull;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.database.repositories.UserRepository;
import nl.tudelft.sem.project.users.exceptions.EmailInUseException;
import nl.tudelft.sem.project.users.exceptions.UserNotFoundException;
import nl.tudelft.sem.project.shared.UsernameInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

/**
 * The users service.
 * Handles Create, Read, Delete operations for the user entity.
 */
@Service
@Validated
public class UserService {

    @Autowired
    transient UserRepository userRepository;

    /**
     * Adds a user to the database.
     *
     * @param user the user which will be added.
     * @return the user which was added with the id filled in.
     * @throws EmailInUseException if the email is already in use.
     * @throws UsernameInUseException if the username is already in use.
     */
    public User addUser(@NonNull User user) throws EmailInUseException, UsernameInUseException {
        boolean existsEmail = this.existsByEmail(user.getEmail());
        if (existsEmail) {
            throw new EmailInUseException("Email is already being used by somebody else.");
        }

        boolean existsUsername = this.existsByUsername(user.getUsername());
        if (existsUsername) {
            throw new UsernameInUseException("Username is already being used by somebody else.");
        }
        return userRepository.save(user);
    }

    /**
     * Deletes a user given the id.
     *
     * @param id the id given.
     * @throws UserNotFoundException if the user was not found.
     */
    public void deleteUserById(@NonNull UUID id) throws UserNotFoundException {
        User foundUser = getUserById(id);
        userRepository.delete(foundUser);
    }

    /**
     * Deletes a user by email.
     *
     * @param email the email given.
     * @throws UserNotFoundException if the user was not found.
     */
    public void deleteUserByEmail(@NonNull UserEmail email) throws UserNotFoundException {
        User foundUser = getUserByEmail(email);
        userRepository.delete(foundUser);
    }

    /**
     * Deletes a user by username.
     *
     * @param username the username given.
     * @throws UserNotFoundException if the user was not found.
     */
    public void deleteUserByUsername(@NonNull Username username) throws UserNotFoundException {
        User foundUser = getUserByUsername(username);
        userRepository.delete(foundUser);
    }

    /**
     * Gets a user by id.
     *
     * @param id the id given.
     * @return the found user.
     * @throws UserNotFoundException if the user was not found.
     */
    public User getUserById(@NonNull UUID id) throws UserNotFoundException {
        var user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }

        throw new UserNotFoundException("User could not be found by id.");
    }

    /**
     * Gets a user by username.
     *
     * @param username the username given.
     * @return the found user.
     * @throws UserNotFoundException if the user was not found.
     */
    public User getUserByUsername(@NonNull Username username) throws UserNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        }

        throw new UserNotFoundException("User could not be found by username.");
    }

    /**
     * Gets a user by email.
     *
     * @param email the email given.
     * @return the found user.
     * @throws UserNotFoundException if the user was not found.
     */
    public User getUserByEmail(@NonNull UserEmail email) throws UserNotFoundException {
        var user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        }

        throw new UserNotFoundException("User could not be found by email.");
    }

    /**
     * Checks if a username is already in use.
     *
     * @param username the username given.
     * @return a boolean indicating if the username exists already.
     */
    public boolean existsByUsername(@NonNull Username username) {
        return userRepository.existsByUsername(username);
    }


    /**
     * Checks if a email is already in use.
     *
     * @param email the email given.
     * @return a boolean indicating if the email exists already.
     */
    public boolean existsByEmail(@NonNull UserEmail email) {
        return userRepository.existsByEmail(email);
    }
}
