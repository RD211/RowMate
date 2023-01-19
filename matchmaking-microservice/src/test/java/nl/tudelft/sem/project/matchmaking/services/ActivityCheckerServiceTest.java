package nl.tudelft.sem.project.matchmaking.services;

import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.CompetitionDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.shared.Organization;
import nl.tudelft.sem.project.users.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ActivityCheckerServiceTest {

    ActivityCheckerService activityCheckerService;

    CompetitionDTO competitionDTO;
    UserDTO userDTO;

    @BeforeEach
    void setup() {
        activityCheckerService = new ActivityCheckerService();
        competitionDTO = new CompetitionDTO(
                UUID.randomUUID(),
                "Delft",
                "jeff_bezos",
                Date.from(Instant.now().plus(10,  ChronoUnit.DAYS)),
                Date.from(Instant.now().plus(11,  ChronoUnit.DAYS)),
                List.of(
                        BoatDTO.builder().availablePositions(List.of(BoatRole.Coach, BoatRole.Cox)).build()
                ),
                true,
                null,
                null
        );

        userDTO = UserDTO.builder()
                .email("tester@test.com")
                .username("tester")
                .isAmateur(true)
                .build();
    }

    @Test
    void isAllowedToParticipateInActivityTraining() {
        var trainingDTO = TrainingDTO.builder()
                .build();

        assertTrue(activityCheckerService
                .isAllowedToParticipateInActivity(trainingDTO, userDTO));
    }

    @Test
    void isAllowedToParticipateInActivityCompetitionTrueNoRestrictions() {
        var userDTO = UserDTO.builder()
                .email("tester@test.com")
                .username("tester")
                .build();

        assertTrue(activityCheckerService
                .isAllowedToParticipateInActivity(competitionDTO, userDTO));
    }

    @Test
    void isAllowedToParticipateInActivityCompetitionTrueAmateur() {
        competitionDTO.setAllowsAmateurs(false);

        userDTO.setAmateur(false);

        assertTrue(activityCheckerService
                .isAllowedToParticipateInActivity(competitionDTO, userDTO));
    }


    @Test
    void isAllowedToParticipateInActivityCompetitionFalseAmateur() {
        competitionDTO.setAllowsAmateurs(false);

        assertFalse(activityCheckerService
                .isAllowedToParticipateInActivity(competitionDTO, userDTO));
    }

    @Test
    void isAllowedToParticipateInActivityCompetitionTrueGender() {
        competitionDTO.setRequiredGender(Gender.Female);
        userDTO.setGender(Gender.Female);
        assertTrue(activityCheckerService
                .isAllowedToParticipateInActivity(competitionDTO, userDTO));
    }

    @Test
    void isAllowedToParticipateInActivityCompetitionFalseGender() {
        competitionDTO.setRequiredGender(Gender.Female);
        userDTO.setGender(Gender.Male);
        assertFalse(activityCheckerService
                .isAllowedToParticipateInActivity(competitionDTO, userDTO));
    }

    @Test
    void isAllowedToParticipateInActivityCompetitionTrueOrganization() {
        competitionDTO.setRequiredOrganization("tu delft");
        userDTO.setOrganization(new Organization("tu delft"));
        assertTrue(activityCheckerService
                .isAllowedToParticipateInActivity(competitionDTO, userDTO));
    }

    @Test
    void isAllowedToParticipateInActivityCompetitionFalseOrganization() {
        competitionDTO.setRequiredOrganization("tu delft");
        userDTO.setOrganization(new Organization("vrije"));
        assertFalse(activityCheckerService
                .isAllowedToParticipateInActivity(competitionDTO, userDTO));
    }

    @Test
    void getAvailableBoatRoles() {
    }

    @Test
    void getTakenPositions() {
    }

    @Test
    void doesBoatRoleHaveFreeSlots() {
    }

    @Test
    void isAllowedToJoinWithTime() {
    }

    @Test
    void isUserEligibleForBoatPosition() {
    }

    @Test
    void isAllowedToRegister() {
    }
}