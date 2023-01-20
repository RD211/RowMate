package nl.tudelft.sem.project.matchmaking.services;

import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.CompetitionDTO;
import nl.tudelft.sem.project.activities.TrainingDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.matchmaking.ActivityFilterDTO;
import nl.tudelft.sem.project.matchmaking.ActivityRequestDTO;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistration;
import nl.tudelft.sem.project.shared.Organization;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UsersClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ActivityCheckerServiceTest {


    CompetitionDTO competitionDTO;
    UserDTO userDTO;

    BoatDTO boatDTO;

    @Mock
    transient UsersClient usersClient;

    @InjectMocks
    ActivityCheckerService activityCheckerService;

    @BeforeEach
    void setup() {
        boatDTO = BoatDTO.builder().availablePositions(new ArrayList<>(Arrays.asList(BoatRole.Coach, BoatRole.Cox))).build();
        competitionDTO = new CompetitionDTO(
                UUID.randomUUID(),
                "Delft",
                "jeff_bezos",
                Date.from(Instant.now().plus(10,  ChronoUnit.DAYS)),
                Date.from(Instant.now().plus(11,  ChronoUnit.DAYS)),
                new ArrayList<>(Arrays.asList(boatDTO)),
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
    void getTakenPositionsOk() {
        var registrations = List.of(
                ActivityRegistration.builder().role(BoatRole.Coach).boat(0).build(),
                ActivityRegistration.builder().role(BoatRole.Cox).boat(0).build());

        assertEquals(List.of(BoatRole.Coach, BoatRole.Cox), activityCheckerService.getTakenPositions(
                registrations, competitionDTO, boatDTO
        ));
    }

    @Test
    void getTakenPositionsOkMoreComplicated() {
        var registrations = List.of(
                ActivityRegistration.builder().role(BoatRole.Coach).boat(0).build(),
                ActivityRegistration.builder().role(BoatRole.Cox).boat(1).build());

        competitionDTO.getBoats().add(BoatDTO.builder().build());
        assertEquals(List.of(BoatRole.Coach), activityCheckerService.getTakenPositions(
                registrations, competitionDTO, boatDTO
        ));
    }

    @Test
    void getTakenPositionsNone() {
        var registrations = List.of(
                ActivityRegistration.builder().role(BoatRole.Coach).boat(1).build(),
                ActivityRegistration.builder().role(BoatRole.Cox).boat(1).build());

        competitionDTO.getBoats().add(BoatDTO.builder().build());
        assertEquals(List.of(), activityCheckerService.getTakenPositions(
                registrations, competitionDTO, boatDTO
        ));
    }

    @Test
    void doesBoatRoleHaveFreeSlotsTrueTrivial() {
        assertTrue(activityCheckerService.doesBoatRoleHaveFreeSlots(
                BoatRole.Coach,
                boatDTO,
                List.of()
        ));
    }

    @Test
    void doesBoatRoleHaveFreeSlotsTrue() {
        boatDTO.getAvailablePositions().add(BoatRole.Coach);
        assertTrue(activityCheckerService.doesBoatRoleHaveFreeSlots(
                BoatRole.Coach,
                boatDTO,
                List.of(BoatRole.Coach)
        ));
    }

    @Test
    void doesBoatRoleHaveFreeSlotsFalseEqual() {
        boatDTO.getAvailablePositions().add(BoatRole.Coach);
        assertFalse(activityCheckerService.doesBoatRoleHaveFreeSlots(
                BoatRole.Coach,
                boatDTO,
                List.of(BoatRole.Coach, BoatRole.Coach)
        ));
    }

    @Test
    void doesBoatRoleHaveFreeSlotsFalseNone() {
        boatDTO.getAvailablePositions().add(BoatRole.Coach);
        assertFalse(activityCheckerService.doesBoatRoleHaveFreeSlots(
                BoatRole.PortSideRower,
                boatDTO,
                List.of(BoatRole.Coach, BoatRole.Coach)
        ));
    }

    @Test
    void doesBoatRoleHaveFreeSlotsFalseNoSense() {
        boatDTO.getAvailablePositions().add(BoatRole.Coach);
        assertFalse(activityCheckerService.doesBoatRoleHaveFreeSlots(
                BoatRole.PortSideRower,
                boatDTO,
                List.of(BoatRole.Coach, BoatRole.Coach, BoatRole.PortSideRower)
        ));
    }

    @Test
    void isAllowedToJoinWithTimeTrueFar() {
        competitionDTO.setStartTime(Date.from(Instant.now().minus(5, ChronoUnit.DAYS)));
        assertTrue(
                activityCheckerService.isAllowedToJoinWithTime(competitionDTO, Instant.now().minus(10, ChronoUnit.DAYS))
        );
    }

    @Test
    void isAllowedToJoinWithTimeTrueBoundary() {
        competitionDTO.setStartTime(Date.from(Instant.now()));
        assertTrue(
                activityCheckerService.isAllowedToJoinWithTime(competitionDTO, Instant.now().minus(31, ChronoUnit.MINUTES))
        );
    }

    @Test
    void isAllowedToJoinWithTimeFalseBoundary() {
        competitionDTO.setStartTime(Date.from(Instant.now()));
        assertFalse(
                activityCheckerService.isAllowedToJoinWithTime(competitionDTO, Instant.now().minus(29, ChronoUnit.MINUTES))
        );
    }

    @Test
    void isAllowedToJoinWithTimeFalseExtremeBoundary() {
        var time = Instant.now();
        competitionDTO.setStartTime(Date.from(time));
        assertFalse(
                activityCheckerService.isAllowedToJoinWithTime(competitionDTO, time.minus(30, ChronoUnit.MINUTES))
        );
    }

    @Test
    void isAllowedToJoinWithTimeFalseExtremeBoundaryExact() {
        var time = Instant.now();
        competitionDTO.setStartTime(Date.from(time));
        assertFalse(
                activityCheckerService.isAllowedToJoinWithTime(competitionDTO, time.minus(60 * 29 + 59, ChronoUnit.SECONDS))
        );
    }

    @Test
    void isAllowedToJoinWithTimeTrueExtremeBoundaryExact() {
        var time = Instant.now();
        competitionDTO.setStartTime(Date.from(time));
        assertTrue(
                activityCheckerService.isAllowedToJoinWithTime(competitionDTO, time.minus(60 * 29 + 61, ChronoUnit.SECONDS))
        );
    }

    @Test
    void isAllowedToJoinWithTimeFalseNoSense() {
        competitionDTO.setStartTime(Date.from(Instant.now().minus(20, ChronoUnit.DAYS)));
        assertFalse(
                activityCheckerService.isAllowedToJoinWithTime(competitionDTO, Instant.now())
        );
    }

    @Test
    void isUserEligibleForBoatPositionNotCox() {
        assertTrue(
                activityCheckerService.isUserEligibleForBoatPosition(
                        new Username("tester"),
                        BoatRole.Coach,
                        boatDTO
                )
        );
    }

    @Test
    void isUserEligibleForBoatPositionCoxTrue() {
        boatDTO.setCoxCertificateId(UUID.randomUUID());
        when(usersClient.hasCertificate(new Username(userDTO.getUsername()), boatDTO.getCoxCertificateId()))
                .thenReturn(true);
        assertTrue(
                activityCheckerService.isUserEligibleForBoatPosition(
                        new Username(userDTO.getUsername()),
                        BoatRole.Cox,
                        boatDTO
                )
        );
    }

    @Test
    void isUserEligibleForBoatPositionCoxFalse() {
        boatDTO.setCoxCertificateId(UUID.randomUUID());
        when(usersClient.hasCertificate(new Username(userDTO.getUsername()), boatDTO.getCoxCertificateId()))
                .thenReturn(false);
        assertFalse(
                activityCheckerService.isUserEligibleForBoatPosition(
                        new Username(userDTO.getUsername()),
                        BoatRole.Cox,
                        boatDTO
                )
        );
    }

    @Test
    void isAllowedToRegisterTrue() {
        boatDTO.setCoxCertificateId(UUID.randomUUID());
        when(usersClient.hasCertificate(new Username(userDTO.getUsername()), boatDTO.getCoxCertificateId()))
                .thenReturn(true);

        competitionDTO.setStartTime(Date.from(Instant.now().plus(5, ChronoUnit.DAYS)));
        assertTrue(activityCheckerService.isAllowedToRegister(competitionDTO, userDTO,
                BoatRole.Coach, boatDTO));
    }

    @Test
    void isAllowedToRegisterFalseTime() {
        boatDTO.setCoxCertificateId(UUID.randomUUID());
        when(usersClient.hasCertificate(new Username(userDTO.getUsername()), boatDTO.getCoxCertificateId()))
                .thenReturn(true);

        competitionDTO.setStartTime(Date.from(Instant.now()));
        assertFalse(activityCheckerService.isAllowedToRegister(competitionDTO, userDTO,
                BoatRole.Coach, boatDTO));
    }

    @Test
    void isAllowedToRegisterFalseRestrictions() {
        boatDTO.setCoxCertificateId(UUID.randomUUID());
        when(usersClient.hasCertificate(new Username(userDTO.getUsername()), boatDTO.getCoxCertificateId()))
                .thenReturn(true);

        competitionDTO.setStartTime(Date.from(Instant.now().plus(5, ChronoUnit.DAYS)));
        competitionDTO.setAllowsAmateurs(false);
        userDTO.setAmateur(true);
        assertFalse(activityCheckerService.isAllowedToRegister(competitionDTO, userDTO,
                BoatRole.Coach, boatDTO));
    }


    @Test
    void isAllowedToRegisterFalseCertificate() {
        boatDTO.setCoxCertificateId(UUID.randomUUID());
        when(usersClient.hasCertificate(new Username(userDTO.getUsername()), boatDTO.getCoxCertificateId()))
                .thenReturn(false);

        competitionDTO.setStartTime(Date.from(Instant.now().plus(5, ChronoUnit.DAYS)));
        assertFalse(activityCheckerService.isAllowedToRegister(competitionDTO, userDTO,
                BoatRole.Cox, boatDTO));
    }

    @Test
    void isAllowedToRegisterFalseAll() {
        boatDTO.setCoxCertificateId(UUID.randomUUID());
        when(usersClient.hasCertificate(new Username(userDTO.getUsername()), boatDTO.getCoxCertificateId()))
                .thenReturn(false);

        competitionDTO.setStartTime(Date.from(Instant.now()));
        competitionDTO.setAllowsAmateurs(false);
        userDTO.setAmateur(true);
        assertFalse(activityCheckerService.isAllowedToRegister(competitionDTO, userDTO,
                BoatRole.Cox, boatDTO));
    }

    @Test
    void getAvailableBoatRolesSimple() {
        var request = ActivityRequestDTO.builder().activityFilter(
                ActivityFilterDTO.builder().preferredRoles(List.of(BoatRole.Coach)).build()
        ).userName(userDTO.getUsername()).build();

        var taken = List.of(BoatRole.Cox);
        assertEquals(Stream.of(BoatRole.Coach).collect(Collectors.toList()), activityCheckerService.getAvailableBoatRoles(
                request, boatDTO, taken
        ).collect(Collectors.toList()));
    }

    @Test
    void getAvailableBoatRolesComplexNone() {
        var request = ActivityRequestDTO.builder().activityFilter(
                ActivityFilterDTO.builder().preferredRoles(List.of(BoatRole.Coach, BoatRole.Cox)).build()
        ).userName(userDTO.getUsername()).build();

        when(usersClient.hasCertificate(new Username(userDTO.getUsername()), boatDTO.getCoxCertificateId()))
                .thenReturn(false);

        List<BoatRole> taken = List.of(BoatRole.Coach);
        assertEquals(Stream.of().collect(Collectors.toList()), activityCheckerService.getAvailableBoatRoles(
                request, boatDTO, taken
        ).collect(Collectors.toList()));
    }

    @Test
    void getAvailableBoatRolesComplexCox() {
        var request = ActivityRequestDTO.builder().activityFilter(
                ActivityFilterDTO.builder().preferredRoles(List.of(BoatRole.Coach, BoatRole.Cox)).build()
        ).userName(userDTO.getUsername()).build();

        when(usersClient.hasCertificate(new Username(userDTO.getUsername()), boatDTO.getCoxCertificateId()))
                .thenReturn(true);

        List<BoatRole> taken = List.of(BoatRole.Coach);
        assertEquals(Stream.of(BoatRole.Cox).collect(Collectors.toList()), activityCheckerService.getAvailableBoatRoles(
                request, boatDTO, taken
        ).collect(Collectors.toList()));
    }
}