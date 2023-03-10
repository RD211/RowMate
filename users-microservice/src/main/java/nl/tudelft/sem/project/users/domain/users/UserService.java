package nl.tudelft.sem.project.users.domain.users;

import lombok.NonNull;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserEmail;
import nl.tudelft.sem.project.users.database.repositories.UserRepository;
import nl.tudelft.sem.project.users.EmailInUseException;
import nl.tudelft.sem.project.users.UserNotFoundException;
import nl.tudelft.sem.project.shared.UsernameInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
        if (credentialsExist(user)) {
            throw new EmailInUseException("A user with the same username or email already exists.");
        }
        return userRepository.save(user);
    }

    private boolean credentialsExist(@NonNull User user) throws EmailInUseException, UsernameInUseException {
        boolean existsEmail = this.existsByEmail(user.getEmail());
        boolean existsUsername = this.existsByUsername(user.getUsername());
        return existsUsername || existsEmail;
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
