package nl.tudelft.sem.project.users.boundary;

import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UsersClient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class UserBoundaryTests {

    @Autowired
    UsersClient usersClient;

    @ParameterizedTest
    @CsvSource({"a", "ab", "abc", "aaa", "aaa",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"})
    void addUserInvalidUsernameTest(String username) {
        var userDTO = UserDTO.builder().email("test@test.com").username(username).build();
        assertThrows(Exception.class, () -> usersClient.addUser(userDTO));
    }

    @ParameterizedTest
    @CsvSource({"tester", "te@", "yh5tuj", ".com", "asdas.com", "@.@", "bhgfbg.@.@.##.com", "sadsda@.com"})
    void addUserInvalidEmailTest(String email) {
        var userDTO = UserDTO.builder().email(email).username("tester").build();
        assertThrows(Exception.class, () -> usersClient.addUser(userDTO));
    }

    @ParameterizedTest
    @CsvSource({"tester", "asdff", "yh5tuj", "sudhifsdfhui",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"})
    void addUserValidUsernameTest(String username) {
        var userDTO = UserDTO.builder().email(username + "user@user.com").username(username).build();
        var user = usersClient.addUser(userDTO);
        assertEquals(user.getUsername(), userDTO.getUsername());
    }

    @ParameterizedTest
    @CsvSource({"dsjklhasdasfklhj@hkjlfsdkhjl.com", "sadsda@a.com"})
    void addUserValidEmailTest(String email) {
        var userDTO = UserDTO.builder().email(email).username(email).build();
        var user = usersClient.addUser(userDTO);
        assertEquals(user.getUsername(), userDTO.getUsername());
    }
}
