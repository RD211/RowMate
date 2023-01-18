package nl.tudelft.sem.project.matchmaking.services;

import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.CompetitionDTO;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.matchmaking.ActivityRequestDTO;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistration;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
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

    /**
     * Checks if the user is allowed to participate in the activity.
     * Checks user data against competition requirements if there are any.
     *
     * @param activity The activity checked.
     * @param user The user which is considering joining the activity.
     * @return Whether the user meets the requirements.
     */
    public boolean isAllowedToParticipateInActivity(
            ActivityDTO activity, UserDTO user
    ) {
        if (!(activity instanceof CompetitionDTO)) {
            return true;
        }
        CompetitionDTO competition = (CompetitionDTO) activity;

        boolean allowsAmateurs = competition.getAllowsAmateurs();
        Gender requiredGender = competition.getRequiredGender();
        String requiredOrganization = competition.getRequiredOrganization();

        return (allowsAmateurs || !user.isAmateur())
                && (requiredGender == null || requiredGender.equals(user.getGender()))
                && (requiredOrganization == null || requiredOrganization.equals(user.getOrganization()));
    }

    /**
     * Gets a stream of boat roles the user can fill.
     * Selected according to the user preferences, certificates and role availabilities.
     *
     * @param dto The activity request.
     * @param boat The checked boat.
     * @param takenRoles The list of roles already filled in the boat.
     * @return A stream of possible boat roles the user can choose to fill.
     */
    public Stream<BoatRole> getAvailableBoatRoles(
            ActivityRequestDTO dto, BoatDTO boat, List<BoatRole> takenRoles
    ) {

        var preferredRoles = dto.getActivityFilter().getPreferredRoles();
        return preferredRoles.stream()
                .filter(r -> doesBoatRoleHaveFreeSlots(r, boat, takenRoles))
                .filter(r -> isUserEligibleForBoatPosition(new Username(dto.getUserName()), r, boat));
    }


    /**
     * Filters activity registrations for a given boat from an activity.
     *
     * @param registrations The list of all registrations of the activity.
     * @param activity The activity.
     * @param boat The boat.
     * @return List of boat roles already taken in this boat.
     */
    public List<BoatRole> getTakenPositions(List<ActivityRegistration> registrations, ActivityDTO activity, BoatDTO boat) {
        return registrations.stream()
                .filter(ar -> activity.getBoats().get(ar.getBoat()).equals(boat))
                .map(ar -> ar.getRole())
                .collect(Collectors.toList());
    }

    /**
     * Checks whether there are enough positions remaining of the role in the boat.
     *
     * @param role The role requested.
     * @param boat The boat to check for availability.
     * @param filledPositions The list of positions already occupied in the boat.
     * @return Whether the role is still available in the boat.
     */
    public boolean doesBoatRoleHaveFreeSlots(final BoatRole role, BoatDTO boat, List<BoatRole> filledPositions) {
        long rolePositionsInBoat = boat.getAvailablePositions().stream().filter(p -> p.equals(role)).count();
        long filledRolePositions = filledPositions.stream().filter(p -> p.equals(role)).count();
        return filledRolePositions < rolePositionsInBoat;
    }

    /**
     * Checks whether one can still join the activity at some point in time.
     * The activities close for registration some time before their start.
     *
     * @param activity Activity to check for.
     * @param now The time instant to check for.
     * @return Whether a user is still allowed to join the activity.
     */
    public boolean isAllowedToJoinWithTime(ActivityDTO activity, Instant now) {
        Instant activityTime = activity.getStartTime().toInstant();
        Instant shouldJoinBefore = activityTime.minusSeconds(secondsToActivityStart);
        return now.compareTo(shouldJoinBefore) <= 0;
    }

    /**
     * Checks whether the user has the certificate for the requested boat.
     * If the user is not applying for cox position, this will always return true.
     *
     * @param username The name of the user that should be checked.
     * @param position The boat position the user is applying for.
     * @return Whether the user is eligible for the position.
     */
    boolean isUserEligibleForBoatPosition(Username username, BoatRole position, BoatDTO boat) {
        if (!position.equals(BoatRole.Cox)) {
            return true;
        }
        UUID requiredCertificateId = boat.getCoxCertificateId();
        return usersClient.hasCertificate(username, requiredCertificateId);
    }

    /**
     * Checks requirements for joining an activity for the specified boat for the selected position.
     * Checks:
     *  - Activity type, i.e. if competition allows this kind of user
     *  - User certificates if the position requires
     *  - If it is not too late to join the activity
     *
     * @param activity The activity the user is trying to register to.
     * @param user The user that is trying to get registered.
     * @param role The role the user is trying to register for.
     * @param boat The boat the user is trying to register for.
     * @return Whether the user will be allowed to register.
     */
    public boolean isAllowedToRegister(ActivityDTO activity, UserDTO user, BoatRole role, BoatDTO boat) {
        return isAllowedToParticipateInActivity(activity, user)
                 && isUserEligibleForBoatPosition(new Username(user.getUsername()), role, boat)
                 && isAllowedToJoinWithTime(activity, Instant.now());
    }

}
