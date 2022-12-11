package nl.tudelft.sem.project.users.domain.users;

import nl.tudelft.sem.project.entities.users.UserDTO;
import nl.tudelft.sem.project.users.domain.users.User;
import nl.tudelft.sem.project.users.domain.users.UserEmail;
import nl.tudelft.sem.project.users.domain.users.Username;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void testToDTO() {
        var id = UUID.randomUUID();

        User user = User.builder()
                .id(id)
                .username(new Username("ane"))
                .email(new UserEmail("ane@ane.com"))
                .build();

        var dto = user.toDTO();
        assertEquals(id, dto.getId());
        assertEquals("ane", dto.getUsername());
        assertEquals("ane@ane.com", dto.getEmail());

    }

    @Test
    void testFromDTOConstructor() {
        var id = UUID.randomUUID();

        UserDTO dto = UserDTO.builder()
                .id(id)
                .username("ane")
                .email("ane@ane.com")
                .build();

        var user = new User(dto);
        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getUsername(), user.getUsername().toString());
        assertEquals(dto.getEmail(), user.getEmail().toString());
    }
}