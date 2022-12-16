package nl.tudelft.sem.project.users.integration;

import feign.FeignException;
import nl.tudelft.sem.project.shared.DateInterval;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UsersClient;
import nl.tudelft.sem.project.users.models.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class FeignClientTest {
    @Autowired
    UsersClient usersClient;


    @ParameterizedTest
    @CsvSource({"test,test@test.com", "tEst,tester@gmail.com", "amazing_name_here,wow@wow.com"})
    void addUserTest(String username, String email) {
        var response = usersClient.addUser(
                UserDTO.builder().email(email).username(username).build()
        );

        assertNotNull(response.getId());
        assertEquals(username, response.getUsername());
        assertEquals(email, response.getEmail());
    }

    @ParameterizedTest
    @CsvSource({"test,testtest.com", "tEst,teste@r@gmail", "amazing_name_here,wow@w@ow.com"})
    void addUserInvalidEmailTest(String username, String email) {
        assertThrows(FeignException.class, () -> usersClient.addUser(
                    UserDTO.builder().email(email).username(username).build()
        ));
    }

    @ParameterizedTest
    @CsvSource({
            "a,test@test.com",
            "as,tester@gmail.com",
            "ads,email@com.com",
            "123456789123456789123456789123456789123456789123456789123456789,email@email.com"
    })
    void addUserInvalidUsernameTest(String username, String email) {
        assertThrows(FeignException.class, () -> usersClient.addUser(
                UserDTO.builder().email(email).username(username).build()
        ));
    }

    @ParameterizedTest
    @CsvSource({"testid,test@testid.com", "tEstid,tester@gmailid.com", "amazing_name_hereid,wow@woidw.com"})
    void getUserByIdTest(String username, String email) {
        var response = usersClient.addUser(
                UserDTO.builder().email(email).username(username).build()
        );

        var newResponse = usersClient.getUserById(response.getId());

        assertEquals(response, newResponse);
    }

    @ParameterizedTest
    @CsvSource({"testidu,test@testidu.com", "tEstidu,tester@gmailuid.com", "amazing_name_heureid,wuow@woidw.com"})
    void getUserByUsernameTest(String username, String email) {
        var response = usersClient.addUser(
                UserDTO.builder().email(email).username(username).build()
        );

        var newResponse = usersClient.getUserByUsername(new Username(response.getUsername()));

        assertEquals(response, newResponse);
    }

    @Test
    void addAvailabilityToUserTest() {
        var userDTO = UserDTO.builder().email("email1@email.com").username("tester1").build();
        var newDate = new DateInterval(
                Date.from(Instant.now().plus(10, ChronoUnit.DAYS)),
                Date.from(Instant.now().plus(50, ChronoUnit.DAYS))
        );
        var userToSend = usersClient.addUser(
                userDTO
        );

        var model = new AddAvailabilityUserModel(
                userToSend,
                newDate
        );

        var response = usersClient.addAvailabilityToUser(
                model
        );

        assertTrue(response.getAvailableTime().contains(newDate));
    }

    @Test
    void removeAvailabilityOfUserTest() {
        var available = new DateInterval(
                Date.from(Instant.now().plus(10, ChronoUnit.DAYS)),
                Date.from(Instant.now().plus(50, ChronoUnit.DAYS))
        );
        var userDTO = UserDTO.builder()
                .email("email@email2.com")
                .username("tester3")
                .availableTime(
                        new HashSet<>(List.of(available))
                )
                .build();

        var userToSend = usersClient.addUser(
                userDTO
        );

        var model = new RemoveAvailabilityUserModel(
                userToSend,
                available
        );

        var response = usersClient.removeAvailabilityOfUser(
                model
        );

        assertTrue(response.getAvailableTime().isEmpty());
    }

    @Test
    void changeGenderOfUserTest() {
        var userDTO = UserDTO.builder()
                .email("email@emai5l.com")
                .username("tester7")
                .gender(Gender.Female)
                .build();
        var newGender = Gender.Male;

        var userToSend = usersClient.addUser(
                userDTO
        );

        var model = new ChangeGenderUserModel(
                userToSend,
                newGender
        );

        var response = usersClient.changeGenderOfUser(
                model
        );

        assertEquals(newGender, response.getGender());
    }

    @Test
    void changeAmateurOfUserTest() {
        var userDTO = UserDTO.builder()
                .email("email@emai54l.com")
                .username("tester74")
                .isAmateur(true)
                .build();

        var userToSend = usersClient.addUser(
                userDTO
        );

        var model = new ChangeAmateurUserModel(
                userToSend,
                false
        );

        var response = usersClient.changeAmateurOfUser(
                model
        );

        assertFalse(response.isAmateur());
    }

    @Test
    void addRoleToUserTest() {
        var userDTO = UserDTO.builder().email("emai23l@email.com").username("te54ster").build();
        var newRole = BoatRole.Coach;

        var userToSend = usersClient.addUser(
                userDTO
        );

        var model = new AddRoleUserModel(
                userToSend,
                newRole
        );

        var response = usersClient.addRoleToUser(
                model
        );

        assertTrue(response.getBoatRoles().contains(newRole));
    }

    @Test
    void removeRoleFromUserTest() {
        var role = BoatRole.Coach;
        var userDTO = UserDTO.builder()
                .email("em4ail@email6.com")
                .username("teste42r")
                .boatRoles(
                        new HashSet<>(List.of(role))
                )
                .build();

        var userToSend = usersClient.addUser(
                userDTO
        );

        var model = new RemoveRoleUserModel(
                userToSend,
                role
        );

        var response = usersClient.removeRoleFromUser(
                model
        );

        assertTrue(response.getBoatRoles().isEmpty());
    }
}
