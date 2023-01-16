package nl.tudelft.sem.project.matchmaking.unit;

import nl.tudelft.sem.project.enums.MatchmakingStrategy;
import nl.tudelft.sem.project.matchmaking.ActivityDeregisterRequestDTO;
import nl.tudelft.sem.project.matchmaking.ActivityRegistrationRequestDTO;
import nl.tudelft.sem.project.matchmaking.ActivityRegistrationResponseDTO;
import nl.tudelft.sem.project.matchmaking.ActivityRequestDTO;
import nl.tudelft.sem.project.matchmaking.controllers.MatchmakingController;
import nl.tudelft.sem.project.matchmaking.services.MatchmakingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MatchmakingControllerTest {
    @MockBean
    transient MatchmakingService matchmakingService;

    @Autowired
    transient MatchmakingController matchmakingController;

    @Test
    public void testList() {
        when(matchmakingService.findActivities(any())).thenReturn(new ArrayList<>());
        assertThat(matchmakingController.findActivities(ActivityRequestDTO.builder().build()))
            .isEqualTo(ResponseEntity.ok(new ArrayList<>()));
    }

    @Test
    public void testAutoFindActivity() {
        when(matchmakingService.autoFindActivity(any(), any())).thenReturn("Successful!");
        assertThat(matchmakingController.autoFindActivity(MatchmakingStrategy.Random, ActivityRequestDTO.builder().build()))
            .isEqualTo(ResponseEntity.ok("Successful!"));
    }

    @Test
    public void testRegisterFalse() {
        when(matchmakingService.registerUserInActivity(any())).thenReturn(false);
        assertThatThrownBy(() -> {
            matchmakingController.registerInActivity(ActivityRegistrationRequestDTO.builder().build());
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void testRegisterTrue() {
        when(matchmakingService.registerUserInActivity(any())).thenReturn(true);
        assertThat(matchmakingController.registerInActivity(ActivityRegistrationRequestDTO.builder().build()))
            .isEqualTo(ResponseEntity.ok("Registration successful."));
    }

    @Test
    public void testDeRegisterFalse() {
        when(matchmakingService.deregisterUserFromActivity(any())).thenReturn(false);
        assertThatThrownBy(() -> {
            matchmakingController.deRegisterFromActivity(ActivityDeregisterRequestDTO.builder().build());
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void testDeRegisterTrue() {
        when(matchmakingService.deregisterUserFromActivity(any())).thenReturn(true);
        assertThat(matchmakingController.deRegisterFromActivity(ActivityDeregisterRequestDTO.builder().build()))
                .isEqualTo(ResponseEntity.ok("You successfully deregistered."));
    }

    @Test
    public void testRespondFalse() {
        when(matchmakingService.respondToRegistration(any())).thenReturn(false);
        assertThatThrownBy(() -> {
            matchmakingController.respondToRegistration(ActivityRegistrationResponseDTO.builder().build());
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void testRespondTrue() {
        when(matchmakingService.respondToRegistration(any())).thenReturn(true);
        assertThat(matchmakingController.respondToRegistration(ActivityRegistrationResponseDTO.builder().build()))
                .isEqualTo(ResponseEntity.ok("Your response has been processed and the user has been notified."));
    }

    @Test
    public void testWaiting() {
        when(matchmakingService.getAllActivitiesThatUserAppliedTo(any())).thenReturn(new ArrayList<>());
        assertThat(matchmakingController.getWaitingApplications("user"))
                .isEqualTo(ResponseEntity.ok(new ArrayList<>()));
    }

    @Test
    public void testAccepted() {
        when(matchmakingService.getAllActivitiesThatUserIsPartOf(any())).thenReturn(new ArrayList<>());
        assertThat(matchmakingController.getAcceptedApplications("user"))
                .isEqualTo(ResponseEntity.ok(new ArrayList<>()));
    }
}
