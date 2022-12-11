package nl.tudelft.sem.project.users.domain.users;

import nl.tudelft.sem.project.users.database.repositories.UserRepository;
import nl.tudelft.sem.project.users.exceptions.EmailInUseException;
import nl.tudelft.sem.project.users.exceptions.UserNotFoundException;
import nl.tudelft.sem.project.users.exceptions.UsernameInUseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    User joe;
    User susJoe;
    User michael;
    User susMichael;
    User richard;
    User otherRichard;

    @BeforeEach
    void setUp() {
        joe = User.builder()
                .id(UUID.randomUUID())
                .username(new Username("Joey"))
                .email(new UserEmail("Joe@joe.joe"))
                .build();
        susJoe = User.builder()
                .id(UUID.randomUUID())
                .username(new Username("Joey"))
                .email(new UserEmail("Joe2@joe.joe"))
                .build();
        michael = User.builder()
                .id(UUID.randomUUID())
                .username(new Username("Michael"))
                .email(new UserEmail("mike@mike.com"))
                .build();
        susMichael = User.builder()
                .id(UUID.randomUUID())
                .username(new Username("Michaele"))
                .email(new UserEmail("mike@mike.com"))
                .build();
        richard = User.builder()
                .id(UUID.randomUUID())
                .username(new Username("Rich"))
                .email(new UserEmail("rich@rich.rich"))
                .build();
        otherRichard = User.builder()
                .id(UUID.randomUUID())
                .username(new Username("Richy"))
                .email(new UserEmail("richy@richy.com"))
                .build();

        when(userRepository.findByEmail(joe.getEmail())).thenReturn(Optional.ofNullable(joe));
        when(userRepository.findByEmail(michael.getEmail())).thenReturn(Optional.ofNullable(michael));
        when(userRepository.findByEmail(richard.getEmail())).thenReturn(Optional.ofNullable(richard));

        when(userRepository.findById(joe.getId())).thenReturn(Optional.ofNullable(joe));
        when(userRepository.findById(michael.getId())).thenReturn(Optional.ofNullable(michael));
        when(userRepository.findById(richard.getId())).thenReturn(Optional.ofNullable(richard));

        when(userRepository.findById(susJoe.getId())).thenReturn(Optional.empty());
        when(userRepository.findById(susMichael.getId())).thenReturn(Optional.empty());
        when(userRepository.findById(otherRichard.getId())).thenReturn(Optional.empty());

        when(userRepository.findByUsername(joe.getUsername())).thenReturn(Optional.ofNullable(joe));
        when(userRepository.findByUsername(michael.getUsername())).thenReturn(Optional.ofNullable(michael));
        when(userRepository.findByUsername(richard.getUsername())).thenReturn(Optional.ofNullable(richard));

        when(userRepository.findByUsername(susJoe.getUsername())).thenReturn(Optional.ofNullable(joe));
        when(userRepository.findByUsername(susMichael.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(otherRichard.getUsername())).thenReturn(Optional.empty());

        when(userRepository.existsByUsername(joe.getUsername())).thenReturn(true);
        when(userRepository.existsByUsername(michael.getUsername())).thenReturn(true);
        when(userRepository.existsByUsername(richard.getUsername())).thenReturn(true);

        when(userRepository.existsByEmail(joe.getEmail())).thenReturn(true);
        when(userRepository.existsByEmail(michael.getEmail())).thenReturn(true);
        when(userRepository.existsByEmail(richard.getEmail())).thenReturn(true);

        when(userRepository.existsByUsername(susJoe.getUsername())).thenReturn(true);
        when(userRepository.existsByUsername(susMichael.getUsername())).thenReturn(false);
        when(userRepository.existsByUsername(otherRichard.getUsername())).thenReturn(false);

        when(userRepository.existsByEmail(susJoe.getEmail())).thenReturn(false);
        when(userRepository.existsByEmail(susMichael.getEmail())).thenReturn(true);
        when(userRepository.existsByEmail(otherRichard.getEmail())).thenReturn(false);

        when(userRepository.findByEmail(susJoe.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(susMichael.getEmail())).thenReturn(Optional.ofNullable(michael));
        when(userRepository.findByEmail(otherRichard.getEmail())).thenReturn(Optional.empty());

        when(userRepository.save(otherRichard)).thenReturn(otherRichard);

    }

    @Test
    void addUserValid() {

        assertEquals(otherRichard, userService.addUser(otherRichard));
        verify(userRepository, times(1)).save(otherRichard);
    }

    @Test
    void addUserInvalidUsername() {
        assertThrows(UsernameInUseException.class, () -> userService.addUser(susJoe));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void addUserInvalidEmail() {
        assertThrows(EmailInUseException.class, () -> userService.addUser(susMichael));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void deleteUserByIdValid() {
        assertDoesNotThrow(() -> userService.deleteUserById(joe.getId()));
        verify(userRepository, times(1)).delete(joe);
    }

    @Test
    void deleteUserByIdInvalid() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(susJoe.getId()));
        verify(userRepository, times(0)).delete(any(User.class));
    }

    @Test
    void deleteUserByEmailValid() {
        assertDoesNotThrow(() -> userService.deleteUserByEmail(joe.getEmail()));
        verify(userRepository, times(1)).delete(joe);
    }

    @Test
    void deleteUserByEmailInvalid() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserByEmail(susJoe.getEmail()));
        verify(userRepository, times(0)).delete(any(User.class));
    }

    @Test
    void deleteUserByUsernameValid() {
        assertDoesNotThrow(() -> userService.deleteUserByUsername(michael.getUsername()));
        verify(userRepository, times(1)).delete(michael);
    }

    @Test
    void deleteUserByUsernameInvalid() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserByUsername(susMichael.getUsername()));
        verify(userRepository, times(0)).delete(any(User.class));
    }

    @Test
    void getUserByIdValid() {
        assertEquals(joe, userService.getUserById(joe.getId()));
        verify(userRepository, times(1)).findById(joe.getId());
    }

    @Test
    void getUserByIdInvalid() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(susJoe.getId()));
    }

    @Test
    void getUserByUsernameValid() {
        assertEquals(joe, userService.getUserByUsername(joe.getUsername()));
        verify(userRepository, times(1)).findByUsername(joe.getUsername());
    }

    @Test
    void getUserByUsernameInvalid() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername(susMichael.getUsername()));
    }

    @Test
    void getUserByEmailValid() {
        assertEquals(michael, userService.getUserByEmail(michael.getEmail()));
        verify(userRepository, times(1)).findByEmail(michael.getEmail());
    }

    @Test
    void getUserByEmailInvalid() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(susJoe.getEmail()));
    }

    @Test
    void existsByUsernameTrue() {
        assertTrue(userService.existsUsername(joe.getUsername()));
    }

    @Test
    void existsByUsernameFalse() {
        assertFalse(userService.existsUsername(susMichael.getUsername()));
    }

    @Test
    void existsByEmailTrue() {
        assertTrue(userService.existsByEmail(michael.getEmail()));
    }

    @Test
    void existsByEmailFalse() {
        assertFalse(userService.existsByEmail(susJoe.getEmail()));
    }
}