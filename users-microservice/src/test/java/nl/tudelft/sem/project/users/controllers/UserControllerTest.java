package nl.tudelft.sem.project.users.controllers;

import nl.tudelft.sem.project.shared.DateInterval;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.database.repositories.UserRepository;
import nl.tudelft.sem.project.users.domain.users.*;
import nl.tudelft.sem.project.users.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserControllerTest {

    @Mock
    transient UserRepository userRepository;

    @Mock
    transient UserService userService;

    @Mock
    transient UserConverterService userConverterService;

    @InjectMocks
    UserController userController;

    @Test
    void addUserTest() {
        var uuid = UUID.randomUUID();
        var userDTO = UserDTO.builder().email("user@user.com").username("user").build();
        var user = User.builder().username(new Username("user")).email(new UserEmail("user@user.com")).build();
        var savedUser =
                User.builder().id(uuid).username(new Username("user")).email(new UserEmail("user@user.com")).build();
        var savedUserDTO = UserDTO.builder().id(uuid).email("user@user.com").username("user").build();
        when(userConverterService.toEntity(userDTO)).thenReturn(user);
        when(userService.addUser(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);

        assertEquals(savedUserDTO, userController.addUser(userDTO).getBody());

        verify(userConverterService, times(1)).toEntity(userDTO);
        verify(userService, times(1)).addUser(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService);

    }

    @Test
    void getUserByIdTest() {
        var uuid = UUID.randomUUID();
        var userDTO = UserDTO.builder().id(uuid).email("user@user.com").username("user").build();
        var user =
                User.builder().id(uuid).username(new Username("user")).email(new UserEmail("user@user.com")).build();

        when(userService.getUserById(uuid)).thenReturn(user);
        when(userConverterService.toDTO(user)).thenReturn(userDTO);

        assertEquals(userDTO, userController.getUserById(uuid).getBody());

        verify(userService, times(1)).getUserById(uuid);
        verify(userConverterService, times(1)).toDTO(user);
        verifyNoMoreInteractions(userConverterService, userService);
    }

    @Test
    void changeGenderTest() {
        var uuid = UUID.randomUUID();
        var userDTO = UserDTO.builder()
                .id(uuid)
                .email("user@user.com")
                .username("user")
                .gender(Gender.NonBinary)
                .build();

        var user =
                User.builder()
                        .id(uuid)
                        .username(new Username("user"))
                        .email(new UserEmail("user@user.com"))
                        .gender(Gender.NonBinary)
                        .build();

        var savedUser = User.builder()
                .id(uuid)
                .username(new Username("user"))
                .email(new UserEmail("user@user.com"))
                .gender(Gender.Female)
                .build();

        var savedUserDTO = UserDTO.builder()
                .id(uuid)
                .email("user@user.com")
                .username("user")
                .gender(Gender.Female)
                .build();

        final var changeGenderModel = new ChangeGenderUserModel(
                userDTO,
                Gender.Female
        );

        when(userConverterService.toDatabaseEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);

        assertEquals(savedUserDTO, userController.changeGender(changeGenderModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService, userRepository);
    }

    @Test
    void addAvailabilityTest() {
        Set<DateInterval> initialTimes = new HashSet<>(
                List.of(new DateInterval(
                        Date.from(Instant.now()),
                        Date.from(Instant.now().plus(10, ChronoUnit.DAYS))))
        );

        DateInterval newDateInterval = new DateInterval(
                Date.from(Instant.now().plus(10, ChronoUnit.DAYS)),
                Date.from(Instant.now().plus(50, ChronoUnit.DAYS))
        );

        Set<DateInterval> newTimes = new HashSet<>(
                List.of(
                        new DateInterval(
                                Date.from(Instant.now()),
                                Date.from(Instant.now().plus(10, ChronoUnit.DAYS))),
                        newDateInterval
                )
        );

        var uuid = UUID.randomUUID();
        var userDTO = UserDTO.builder()
                .id(uuid)
                .email("user@user.com")
                .username("user")
                .availableTime(initialTimes)
                .build();

        var user =
                User.builder()
                        .id(uuid)
                        .username(new Username("user"))
                        .email(new UserEmail("user@user.com"))
                        .availableTime(initialTimes)
                        .build();

        var savedUser = User.builder()
                .id(uuid)
                .username(new Username("user"))
                .email(new UserEmail("user@user.com"))
                .availableTime(newTimes)
                .build();

        var savedUserDTO = UserDTO.builder()
                .id(uuid)
                .email("user@user.com")
                .username("user")
                .availableTime(newTimes)
                .build();

        final var addAvailabilityModel = new AddAvailabilityUserModel(
                userDTO,
                newDateInterval
        );

        when(userConverterService.toDatabaseEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);

        assertEquals(savedUserDTO, userController.addAvailability(addAvailabilityModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService, userRepository);
    }

    @Test
    void removeAvailabilityTest() {
        Set<DateInterval> initialTimes = new HashSet<>(
                List.of(new DateInterval(
                        Date.from(Instant.now()),
                        Date.from(Instant.now().plus(10, ChronoUnit.DAYS))),
                        new DateInterval(
                                Date.from(Instant.now().plus(10, ChronoUnit.DAYS)),
                                Date.from(Instant.now().plus(50, ChronoUnit.DAYS))
                        ))
        );

        DateInterval removeDateInterval = new DateInterval(
                Date.from(Instant.now().plus(10, ChronoUnit.DAYS)),
                Date.from(Instant.now().plus(50, ChronoUnit.DAYS))
        );

        Set<DateInterval> newTimes = new HashSet<>(
                List.of(
                        new DateInterval(
                                Date.from(Instant.now()),
                                Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                )
        );

        var uuid = UUID.randomUUID();
        var userDTO = UserDTO.builder()
                .id(uuid)
                .email("user@user.com")
                .username("user")
                .availableTime(initialTimes)
                .build();

        var user =
                User.builder()
                        .id(uuid)
                        .username(new Username("user"))
                        .email(new UserEmail("user@user.com"))
                        .availableTime(initialTimes)
                        .build();

        var savedUser = User.builder()
                .id(uuid)
                .username(new Username("user"))
                .email(new UserEmail("user@user.com"))
                .availableTime(newTimes)
                .build();

        var savedUserDTO = UserDTO.builder()
                .id(uuid)
                .email("user@user.com")
                .username("user")
                .availableTime(newTimes)
                .build();

        final var removeAvailabilityModel = new RemoveAvailabilityUserModel(
                userDTO,
                removeDateInterval
        );

        when(userConverterService.toDatabaseEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);

        assertEquals(savedUserDTO, userController.removeAvailability(removeAvailabilityModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService, userRepository);
    }

    @Test
    void addRoleTest() {
        Set<BoatRole> initialRoles = new HashSet<>(
                List.of(BoatRole.Coach)
        );

        BoatRole newRole = BoatRole.Cox;

        Set<BoatRole> newRoles = new HashSet<>(
                List.of(BoatRole.Coach, newRole)
        );

        var uuid = UUID.randomUUID();
        var userDTO = UserDTO.builder()
                .id(uuid)
                .email("user@user.com")
                .username("user")
                .boatRoles(initialRoles)
                .build();

        var user =
                User.builder()
                        .id(uuid)
                        .username(new Username("user"))
                        .email(new UserEmail("user@user.com"))
                        .boatRoles(initialRoles)
                        .build();

        var savedUser = User.builder()
                .id(uuid)
                .username(new Username("user"))
                .email(new UserEmail("user@user.com"))
                .boatRoles(newRoles)
                .build();

        var savedUserDTO = UserDTO.builder()
                .id(uuid)
                .email("user@user.com")
                .username("user")
                .boatRoles(newRoles)
                .build();

        final var addRoleUserModel = new AddRoleUserModel(
                userDTO,
                newRole
        );

        when(userConverterService.toDatabaseEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);

        assertEquals(savedUserDTO, userController.addRole(addRoleUserModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService, userRepository);
    }

    @Test
    void removeAvailability() {
        Set<BoatRole> initialRoles = new HashSet<>(
                List.of(BoatRole.Coach, BoatRole.Cox)
        );

        BoatRole removeRole = BoatRole.Coach;

        Set<BoatRole> newRoles = new HashSet<>(
                List.of(BoatRole.Cox)
        );

        var uuid = UUID.randomUUID();
        var userDTO = UserDTO.builder()
                .id(uuid)
                .email("user@user.com")
                .username("user")
                .boatRoles(initialRoles)
                .build();

        var user =
                User.builder()
                        .id(uuid)
                        .username(new Username("user"))
                        .email(new UserEmail("user@user.com"))
                        .boatRoles(initialRoles)
                        .build();

        var savedUser = User.builder()
                .id(uuid)
                .username(new Username("user"))
                .email(new UserEmail("user@user.com"))
                .boatRoles(newRoles)
                .build();

        var savedUserDTO = UserDTO.builder()
                .id(uuid)
                .email("user@user.com")
                .username("user")
                .boatRoles(newRoles)
                .build();

        final var removeRoleUserModel = new RemoveRoleUserModel(
                userDTO,
                removeRole
        );

        when(userConverterService.toDatabaseEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userConverterService.toDTO(savedUser)).thenReturn(savedUserDTO);

        assertEquals(savedUserDTO, userController.removeRole(removeRoleUserModel).getBody());

        verify(userConverterService, times(1)).toDatabaseEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userConverterService, times(1)).toDTO(savedUser);
        verifyNoMoreInteractions(userConverterService, userService, userRepository);
    }
}