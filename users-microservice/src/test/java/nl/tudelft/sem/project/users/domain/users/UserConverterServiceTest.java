package nl.tudelft.sem.project.users.domain.users;

import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserConverterServiceTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserConverterService userConverterService;

    @Test
    void toDTO() {
        var uuid = UUID.randomUUID();
        var user = User.builder()
                .email(new UserEmail("test@test.com"))
                .username(new Username("tester"))
                .build();
        var dto = userConverterService.toDTO(user);

        assertNotNull(dto);
        assertEquals("test@test.com", dto.getEmail());
        assertEquals("tester", dto.getUsername());
    }

    @Test
    void toEntity() {
        var uuid = UUID.randomUUID();
        var dto = UserDTO.builder()
                .email("test@test.com")
                .username("tester")
                .build();
        var user = userConverterService.toEntity(dto);

        assertNotNull(user);
        assertEquals("test@test.com", user.getEmail().getEmail());
        assertEquals("tester", user.getUsername().getName());
    }

    @Test
    void toDatabaseEntity() {
        when(userService.getUserByUsername(new Username("tester"))).thenReturn(
                User.builder()
                        .email(new UserEmail("test@test.com"))
                        .username(new Username("tester"))
                        .build()
        );

        var dto = UserDTO
                .builder().username("tester")
                .build();
        var user = userConverterService.toDatabaseEntity(dto);

        verify(userService, times(1)).getUserByUsername(new Username("tester"));

        assertNotNull(user);
        assertEquals("test@test.com", user.getEmail().getEmail());
        assertEquals("tester", user.getUsername().getName());
    }
}