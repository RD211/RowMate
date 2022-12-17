package nl.tudelft.sem.project.matchmaking.integration;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class ExampleTest {

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    public void helloWorld() throws Exception {
//        ResultActions result = mockMvc.perform(get("/hello")
//                .contentType(MediaType.APPLICATION_JSON));
//        // Assert
//        result.andExpect(status().isOk());
//
//        String response = result.andReturn().getResponse().getContentAsString();
//
//        assertThat(response).isEqualTo("Hello World");
//
//    }
}
