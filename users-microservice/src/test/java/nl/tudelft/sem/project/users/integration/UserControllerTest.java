package nl.tudelft.sem.project.users.integration;

import com.google.gson.Gson;
import nl.tudelft.sem.project.users.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addUserValid() throws Exception {
        Gson gson = new Gson();


        var user = UserDTO.builder()
                .username("test")
                .email("test@tester.com")
                .build();

        var userJson = gson.toJson(user);

        ResultActions result = mockMvc.perform(post("/api/users/add_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        result.andExpect(status().isOk());

        UserDTO response = gson.fromJson(result.andReturn().getResponse().getContentAsString(), UserDTO.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getUsername()).isEqualTo(user.getUsername());
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void addUserInvalid() throws Exception {
        Gson gson = new Gson();


        var user = UserDTO.builder()
                .username("test")
                .email("not good email")
                .build();

        var userJson = gson.toJson(user);

        ResultActions result = mockMvc.perform(post("/api/users/add_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        result.andExpect(status().is4xxClientError());

        String response = result.andReturn().getResponse().getContentAsString();
        assertEquals("email: Email must be valid.", response);
    }

    @Test
    void getUserById() {
    }

    @Test
    void changeGender() {
    }

    @Test
    void addAvailability() {
    }

    @Test
    void removeAvailability() {
    }
}
