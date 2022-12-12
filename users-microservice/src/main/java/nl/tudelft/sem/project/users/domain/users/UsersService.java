package nl.tudelft.sem.project.users.domain.users;

import nl.tudelft.sem.project.users.exceptions.EmailInUseException;
import nl.tudelft.sem.project.users.exceptions.UserNotFoundException;
import nl.tudelft.sem.project.users.exceptions.UsernameInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * The users service.
 * Handles Create, Read, Delete operations for the user entity.
 */
@Service
public class UsersService {

    @Autowired
    private transient UserRepository userRepository;

    /**
     * Adds a user to the database.
     *
     * @param user the user which will be added.
     * @return the user which was added with the id filled in.
     * @throws EmailInUseException if the email is already in use.
     * @throws UsernameInUseException if the username is already in use.
     */
    public User addUser(User user) throws EmailInUseException, UsernameInUseException {
        boolean existsEmail = this.existsByEmail(user.getEmail());
        if (existsEmail) {
            throw new EmailInUseException("Email is already being used by somebody else.");
        }

        boolean existsUsername = this.existsUsername(user.getUsername());
        if (existsUsername) {
            throw new UsernameInUseException("Username is already being used by somebody else.");
        }
        var savedUser = userRepository.save(user);
        return savedUser;
    }

    /**
     * Deletes a user given the id.
     *
     * @param id the id given.
     * @throws UserNotFoundException if the user was not found.
     */
    public void deleteUserById(UUID id) throws UserNotFoundException {
        User foundUser = getUserById(id);
        userRepository.delete(foundUser);
    }

    /**
     * Deletes a user by email.
     *
     * @param email the email given.
     * @throws UserNotFoundException if the user was not found.
     */
    public void deleteUserByEmail(UserEmail email) throws UserNotFoundException {
        User foundUser = getUserByEmail(email);
        userRepository.delete(foundUser);
    }

    /**
     * Deletes a user by username.
     *
     * @param username the username given.
     * @throws UserNotFoundException if the user was not found.
     */
    public void deleteUserByUsername(Username username) throws UserNotFoundException {
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
    public User getUserById(UUID id) throws UserNotFoundException {
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
    public User getUserByUsername(Username username) throws UserNotFoundException {
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
    public User getUserByEmail(UserEmail email) throws UserNotFoundException {
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
    public boolean existsUsername(Username username) {
        return userRepository.existsByUsername(username);
    }


    /**
     * Checks if a email is already in use.
     *
     * @param email the email given.
     * @return a boolean indicating if the email exists already.
     */
    public boolean existsByEmail(UserEmail email) {
        return userRepository.existsByEmail(email);
    }
}