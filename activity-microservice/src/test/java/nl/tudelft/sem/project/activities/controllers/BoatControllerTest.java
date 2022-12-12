package nl.tudelft.sem.project.activities.controllers;

import com.google.gson.Gson;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import org.junit.jupiter.api.BeforeAll;
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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
class BoatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static Gson gson;

    @BeforeAll
    public static void setUp() {
        gson = new Gson();
    }

    @Test
    public void addBoat() throws Exception {
        var boat = BoatDTO.builder()
                .name("Test Boat")
                .availablePositions(List.of(BoatRole.Coach, BoatRole.ScullingRower))
                .build();
        var boatJson = gson.toJson(boat);

        ResultActions result = mockMvc.perform(post("/add_boat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(boatJson));

        result.andExpect(status().isOk());

        BoatDTO response = gson.fromJson(result.andReturn().getResponse().getContentAsString(), BoatDTO.class);

        assertThat(response.getBoatId()).isNotNull();
        assertThat(response.getName()).isEqualTo(boat.getName());
        assertThat(response.getAvailablePositions()).containsExactlyInAnyOrder(BoatRole.Coach, BoatRole.ScullingRower);
    }

    @Test
    public void getBoatFail() throws Exception {

        ResultActions result = mockMvc.perform(get("/get_boat")
                .queryParam("boatId", UUID.randomUUID().toString()));

        result.andExpect(status().is4xxClientError());
        assertThat(result.andReturn().getResponse().getStatus()).isEqualTo(404);
    }

    @Test
    public void getBoatSuccess() throws Exception {

        var boat = BoatDTO.builder()
                .name("Test Boat")
                .availablePositions(List.of(BoatRole.Coach, BoatRole.ScullingRower))
                .build();
        var boatJson = gson.toJson(boat);

        ResultActions postResult = mockMvc.perform(post("/add_boat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(boatJson));

        postResult.andExpect(status().isOk());

        BoatDTO boatResponse = gson.fromJson(postResult.andReturn().getResponse().getContentAsString(), BoatDTO.class);

        ResultActions getResult = mockMvc.perform(get("/get_boat")
                .queryParam("boatId", boatResponse.getBoatId().toString()));

        getResult.andExpect(status().isOk());

        BoatDTO response = gson.fromJson(getResult.andReturn().getResponse().getContentAsString(), BoatDTO.class);

        assertThat(response.getBoatId()).isEqualTo(boatResponse.getBoatId());
        assertThat(response.getName()).isEqualTo(boatResponse.getName());
        assertThat(response.getAvailablePositions()).containsExactlyInAnyOrder(BoatRole.Coach, BoatRole.ScullingRower);
    }
}