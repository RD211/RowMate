package nl.tudelft.sem.project.matchmaking.services;

import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.CompetitionDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistration;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UsersClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ActivityCheckerService {

    @Autowired
    private transient UsersClient usersClient;

    private static final long secondsToActivityStart = 30 * 60;

    public boolean isAllowedToParticipateInActivity(
            ActivityDTO activityDTO, Username username
    ) {
        if (!(activityDTO instanceof CompetitionDTO)) return true;
        CompetitionDTO competitionDTO = (CompetitionDTO) activityDTO;

        var userDTO = usersClient.getUserByUsername(username);

        boolean allowsAmateurs = competitionDTO.getAllowsAmateurs();
        Gender requiredGender = competitionDTO.getRequiredGender();
        String requiredOrganization = competitionDTO.getRequiredOrganization();

        return (allowsAmateurs || !userDTO.isAmateur())
                && (requiredGender == null || requiredGender.equals(userDTO.getGender()))
                && (requiredOrganization == null || requiredOrganization.equals(userDTO.getOrganization()));
    }


    private List<BoatRole> getTakenPositions(List<ActivityRegistration> registrations, ActivityDTO activity, BoatDTO boat) {
        return registrations.stream()
                .filter(ar -> activity.getBoats().get(ar.getBoat()).equals(boat))
                .map(ar -> ar.getRole()).collect(Collectors.toList());
    }

    private Stream<BoatRole> filterOnPreferredPositions(Stream<BoatRole> stream, List<BoatRole> preferredRoles) {
        return stream.filter(p -> preferredRoles.contains(p));
    }

    private Stream<BoatRole> filterOnPositionAvailability(
            Stream<BoatRole> stream, BoatDTO boat, List<BoatRole> takenPositions
    ) {
        return stream.filter(p -> doesBoatPositionHaveFreeSlots(p, boat, takenPositions));
    }

    private Stream<BoatRole> filterOnPositionAllowed(Stream<BoatRole> stream, BoatDTO boat, String userName) {
        return stream.filter(p -> isUserEligibleForBoatPosition(userName, p, boat));
    }

    /**
     * Checks whether there are enough positions remaining of the role in the boat.
     *
     * @param role The role requested.
     * @param boat The boat to check for availability.
     * @param filledPositions The list of positions already occupied in the boat.
     * @return Whether the role is still available in the boat.
     */
    private boolean doesBoatPositionHaveFreeSlots(final BoatRole role, BoatDTO boat, List<BoatRole> filledPositions) {
        long rolePositionsInBoat = boat.getAvailablePositions().stream().filter(p -> p.equals(role)).count();
        long filledRolePositions = filledPositions.stream().filter(p -> p.equals(role)).count();
        return rolePositionsInBoat != filledRolePositions;
    }

    /**
     * Checks whether one can still join the activity at some point in time.
     * The activities close for registration some time before their start.
     *
     * @param activity Activity to check for.
     * @param now The time instant to check for.
     * @return Whether a user is still allowed to join the activity.
     */
    private boolean isAllowedToJoinWithTime(ActivityDTO activity, Instant now) {
        Instant activityTime = activity.getStartTime().toInstant();
        Instant shouldJoinBefore = activityTime.minusSeconds(secondsToActivityStart);
        return now.compareTo(shouldJoinBefore) <= 0;
    }

    /**
     * Checks whether the user has the certificate for the requested boat.
     * If the user is not applying for cox position, this will always return true.
     *
     * @param userName The name of the user that should be checked.
     * @param position The boat position the user is applying for.
     * @return Whether the user is eligible for the position.
     */
    private boolean isUserEligibleForBoatPosition(String userName, BoatRole position, BoatDTO boat) {
        if (!position.equals(BoatRole.Cox)) {
            return true;
        }
        UUID requiredCertificateId = boat.getCoxCertificateId();
        return usersClient.hasCertificate(new Username(userName), requiredCertificateId);
    }

}
