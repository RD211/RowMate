package nl.tudelft.sem.project.matchmaking.services;

import nl.tudelft.sem.project.activities.*;
import nl.tudelft.sem.project.gateway.SeatedUserModel;
import nl.tudelft.sem.project.matchmaking.*;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.MatchmakingStrategy;
import nl.tudelft.sem.project.matchmaking.models.FoundActivityModel;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistration;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistrationId;
import nl.tudelft.sem.project.matchmaking.domain.ActivityRegistrationRepository;
import nl.tudelft.sem.project.matchmaking.models.AvailableActivityModel;
import nl.tudelft.sem.project.matchmaking.strategies.EarliestFirstStrategy;
import nl.tudelft.sem.project.matchmaking.strategies.MatchingStrategy;
import nl.tudelft.sem.project.matchmaking.strategies.RandomStrategy;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UsersClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchmakingService {

    @Autowired
    private transient ActivitiesClient activitiesClient;
    @Autowired
    private transient BoatsClient boatsClient;

    @Autowired
    private transient UsersClient usersClient;
    @Autowired
    private transient ActivityRegistrationRepository activityRegistrationRepository;

    public static final String autoFindErrorMessage =
        "Unfortunately, we could not find any activity matching your request. Please try again!";

    private static final long secondsToActivityStart = 30 * 60;

    public List<ActivityDTO> findActivities(ActivityRequestDTO dto) {
        return activitiesClient.findActivitiesFromFilter(dto.getActivityFilter());
    }

    /**
     * Automatically matches a user with an activity according to their preferred strategy.
     * At the moment, if strategy is 1, the activity with the earliest start is chosen.
     * Otherwise, the system will pick the random strategy.
     *
     * @param strategy the matchmaking strategy
     * @param dto the DTO containing the activity request.
     * @return a string response, meant to inform the user of the activity that was chosen.
     */
    public String autoFindActivity(MatchmakingStrategy strategy, ActivityRequestDTO dto) {
        MatchingStrategy activityMatcher;
        if (strategy == MatchmakingStrategy.EarliestFirst) {
            activityMatcher = new EarliestFirstStrategy();
        } else {
            activityMatcher = new RandomStrategy();
        }

        List<ActivityDTO> availableActivitiesInTimeslot = findActivities(dto);

        activityMatcher.setAvailableActivities(extractFeasibleActivities(dto, availableActivitiesInTimeslot));
        activityMatcher.setRequestData(dto);

        FoundActivityModel pickedActivity = activityMatcher.findActivityToRegister();

        if (pickedActivity != null) {
            registerUserInActivity(pickedActivity.getRegistrationRequestDTO());

            return "Your registration request has been sent to the activity owner. "
                    + "You will get an email when the owner responds to your request.";
        }
        return autoFindErrorMessage;
    }



    /**
     * Given a list of available activities in the timeslot,
     * this function returns the ones that the user can participate in with one of their
     * preferred roles and a boat in which they can be placed.
     *
     * @param dto the request DTO
     * @param availableActivities the list of the available activities.
     * @return a list of available activity models,
     *      containing the activity, the boat index and the role in which the user can be registered.
     */
    private List<AvailableActivityModel> extractFeasibleActivities(
        ActivityRequestDTO dto,
        List<ActivityDTO> availableActivities
    ) {
        List<AvailableActivityModel> feasibleActivities = new ArrayList<>();
        for (ActivityDTO activity : availableActivities) {
            feasibleActivities.addAll(generateSuitableActivities(dto, activity));
        }
        return feasibleActivities;
    }

    private boolean userCanParticipateInCompetition(
            CompetitionDTO competitionDTO,
            String userName
    ) {
        var userDTO = usersClient.getUserByUsername(new Username(userName));
        if (!competitionDTO.getAllowsAmateurs() && userDTO.isAmateur()) {
            return false;
        }
        if (competitionDTO.getRequiredGender() != null
                && !competitionDTO.getRequiredGender().equals(userDTO.getGender())) {
            return false;
        }
        if (competitionDTO.getRequiredOrganization() != null
                && !competitionDTO.getRequiredOrganization().equals(userDTO.getOrganization().toString())) {
            return false;
        }
        return true;
    }

    /**
     * Generates a list of tasks for a user from a given activity.
     * The following are taken into account when selecting:
     *  - User certificates
     *  - User position preference
     *  - Position availability
     *  - Competition
     *
     * @param dto The activity request.
     * @param activity The activity to check.
     * @return The list of tasks the user can perform during this activity.
     */
    @SuppressWarnings("PMD")
    private List<AvailableActivityModel> generateSuitableActivities(ActivityRequestDTO dto, ActivityDTO activity) {
        if (activity instanceof CompetitionDTO
                && !userCanParticipateInCompetition((CompetitionDTO) activity, dto.getUserName())) {
            return new ArrayList<>();
        }

        List<ActivityRegistration> registrations
                = activityRegistrationRepository.findAllByActivityId(activity.getId());

        List<AvailableActivityModel> result = new ArrayList<>();
        for (int i = 0; i < activity.getBoats().size(); i++) {
            BoatDTO boat = activity.getBoats().get(i);
            var takenPositions = registrations.stream()
                    .filter(ar -> activity.getBoats().get(ar.getBoat()).equals(boat))
                    .map(ar -> ar.getRole()).collect(Collectors.toList());

            var preferredBoatPositions = boat.getAvailablePositions().stream()
                    .filter(p -> dto.getActivityFilter().getPreferredRoles().contains(p))
                    .distinct();
            var availablePositions = preferredBoatPositions
                    .filter(p -> doesBoatPositionHaveFreeSlots(p, boat, takenPositions));
            var allowedPositions = availablePositions
                    .filter(p -> isUserEligibleForBoatPosition(dto.getUserName(), p, boat));

            final int boatIdx = i;
            allowedPositions.forEach(p -> result.add(new AvailableActivityModel(activity, boatIdx, p)));
        }
        return result;
    }

    /**
     * Registers a user in an activity.
     *
     * @param dto a DTO containing the information needed for the request.
     * @return true if the registration was successful, e.g. the user is not already registered in the same activity
     *      and the role in the specified boat is available, false otherwise.
     */
    @Transactional
    public boolean registerUserInActivity(ActivityRegistrationRequestDTO dto) {
        ActivityDTO activityDTO = activitiesClient.getActivity(dto.getActivityId());
        if (activityDTO instanceof CompetitionDTO
                && !userCanParticipateInCompetition((CompetitionDTO) activityDTO, dto.getUserName())) {
            return false;
        }


        List<ActivityRegistration> overlappingRegistrations
            = activityRegistrationRepository.findRequestOverlap(
                    dto.getActivityId(),
                    dto.getUserName(),
                    dto.getBoat(),
                    dto.getBoatRole()
            );

        if (!overlappingRegistrations.isEmpty()
                || !isUserEligibleForBoatPosition(
                dto.getUserName(), dto.getBoatRole(), activityDTO.getBoats().get(dto.getBoat()))
                || !isAllowedToJoinWithTime(activityDTO, Instant.now())) {
            return false;
        }

        boolean status
            = activitiesClient.getActivity(dto.getActivityId()).getOwner().equals(dto.getUserName());
        ActivityRegistration registration
            = new ActivityRegistration(
                dto.getUserName(),
                dto.getActivityId(),
                dto.getBoat(),
                dto.getBoatRole(),
                status
            );
        activityRegistrationRepository.save(registration);
        return true;
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
     * De-registers a user from an activity.
     *
     * @param dto a DTO containg the username and activity id.
     * @return true if the de-register was succesful, e.g. the user was indeed registered in the activity,
     *      false otherwise.
     */
    @Transactional
    public boolean deRegisterUserFromActivity(ActivityDeregisterRequestDTO dto) {
        Optional<ActivityRegistration> registration
            = activityRegistrationRepository.findById(new ActivityRegistrationId(dto.getUserName(), dto.getActivityId()));
        if (registration.isEmpty()) {
            return false;
        }

        activityRegistrationRepository.delete(registration.get());
        return true;
    }

    /**
     * Responds to an activity registration request. If the request is accepted,
     * the registration is marked as "accepted".
     * The user will be notified about the response.
     *
     * @param dto a DTO containing the username, activity id and response.
     * @return true if the processing the response was successful, false otherwise.
     */
    @Transactional
    public boolean respondToRegistration(ActivityRegistrationResponseDTO dto) {
        Optional<ActivityRegistration> registration
            = activityRegistrationRepository.findById(new ActivityRegistrationId(dto.getUserName(), dto.getActivityId()));
        if (registration.isEmpty() || registration.get().isAccepted()) {
            return false;
        }

        if (dto.isAccepted()) {
            ActivityRegistration reg = registration.get();
            reg.setAccepted(true);
            activityRegistrationRepository.save(reg);
        } else {
            activityRegistrationRepository.delete(registration.get());
        }
        return true;
    }

    /**
     * Gets a list of the activities that the user has applied to but has not yet been accepted.
     *
     * @param username the username of the user.
     * @return the list of seatedUserModels
     */
    public List<UserActivityApplication> getAllActivitiesThatUserAppliedTo(String username) {
        var seatedModels = activityRegistrationRepository
                .findByUserNameAndAccepted(username, false)
                .stream().map(x -> new SeatedUserModel(x.getActivityId(), x.getBoat(), x.getRole()))
                .collect(Collectors.toList());
        return seatedModels.stream().map(
                x -> {
                    var activity = activitiesClient.getActivity(x.getActivityId());
                    return new UserActivityApplication(
                            activity,
                            activity.getBoats().get(x.getBoat()),
                            x.getBoatRole()
                    );
                }
        ).collect(Collectors.toList());
    }

    /**
     * Gets a list of the activities that the user has been accepted to.
     *
     * @param username the username of the user.
     * @return the list of seatedUserModels
     */
    public List<UserActivityApplication> getAllActivitiesThatUserIsPartOf(String username) {
        var seatedModels = activityRegistrationRepository
                .findByUserNameAndAccepted(username, true)
                .stream().map(x -> new SeatedUserModel(x.getActivityId(), x.getBoat(), x.getRole()))
                .collect(Collectors.toList());
        return seatedModels.stream().map(
                x -> {
                    var activity = activitiesClient.getActivity(x.getActivityId());
                    return new UserActivityApplication(
                      activity,
                            activity.getBoats().get(x.getBoat()),
                            x.getBoatRole()
                    );
                }
        ).collect(Collectors.toList());
    }

    /**
     * Gets all applications to an activity by status(accepted or not yet).
     *
     * @param activityId the activity id.
     * @param accepted the status.
     * @return the list of applications.
     */
    public List<ActivityApplicationModel> getAllApplicationsToActivityByAcceptedStatus(UUID activityId,
                                                                                      boolean accepted) {

        return activityRegistrationRepository
                .findByActivityIdAndAccepted(activityId, accepted).stream()
                .map(x -> new ActivityApplicationModel(x.getUserName(), x.getBoat(), x.getRole()))
                .collect(Collectors.toList());
    }

    /**
     * Deletes a user's registration.
     *
     * @param userName Name of the user.
     * @param activityId The ID of the activity they were registered for.
     */
    public void deleteUserFromActivity(String userName, UUID activityId) {
        activityRegistrationRepository.deleteByUserNameAndActivityId(userName, activityId);
    }
}
