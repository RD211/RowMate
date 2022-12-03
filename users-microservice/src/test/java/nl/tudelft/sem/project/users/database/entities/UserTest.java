package nl.tudelft.sem.project.users.database.entities;

import nl.tudelft.sem.project.entities.users.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testToDTO() {
        var id = UUID.randomUUID();

        User user = User.builder()
                .id(id)
                .username("ane")
                .email("ane@ane.com")
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
        assertEquals(dto.getUsername(), user.getUsername());
        assertEquals(dto.getEmail(), user.getEmail());
    }
}