package nl.tudelft.sem.project.matchmaking.services;

import nl.tudelft.sem.project.activities.*;
import nl.tudelft.sem.project.gateway.SeatedUserModel;
import nl.tudelft.sem.project.matchmaking.*;
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

    @Autowired
    private transient ActivityCheckerService activityCheckerService;

    public static final String autoFindErrorMessage =
        "Unfortunately, we could not find any activity matching your request. Please try again!";


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
        List<AvailableActivityModel> feasibleTasks = new ArrayList<>();
        for (ActivityDTO activity : availableActivities) {
            feasibleTasks.addAll(generateSuitableActivityTasks(dto, activity));
        }
        return feasibleTasks;
    }


    /**
     * Generates a list of tasks for a user from a given activity.
     * The following are taken into account when selecting:
     *  - User certificates for boat roles
     *  - User position preferences
     *  - Boat position availabilities
     *  - Competition activity requirements
     *
     * @param dto The activity request.
     * @param activity The activity to check.
     * @return The list of tasks the user can perform during this activity.
     */
    @SuppressWarnings("PMD")
    private List<AvailableActivityModel> generateSuitableActivityTasks(ActivityRequestDTO dto, ActivityDTO activity) {

        List<AvailableActivityModel> result = new ArrayList<>();
        if (!activityCheckerService.isAllowedToParticipateInActivity(activity, new Username(dto.getUserName()))) {
            return result;
        }

        List<ActivityRegistration> registrations
                = activityRegistrationRepository.findAllByActivityId(activity.getId());

        for (int i = 0; i < activity.getBoats().size(); i++) {
            BoatDTO boat = activity.getBoats().get(i);
            var takenPositions = getTakenPositions(registrations, activity, boat);

            var roles = boat.getAvailablePositions().stream().distinct();
            roles = filterOnPreferredPositions(roles, dto.getActivityFilter().getPreferredRoles());
            roles = filterOnPositionAvailability(roles, boat, takenPositions);
            roles = filterOnPositionAllowed(roles, boat, dto.getUserName());

            final int boatIdx = i;
            roles.forEach(p -> result.add(new AvailableActivityModel(activity, boatIdx, p)));
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
     * De-registers a user from an activity.
     *
     * @param dto a DTO containg the username and activity id.
     * @return true if the de-register was succesful, e.g. the user was indeed registered in the activity,
     *      false otherwise.
     */
    @Transactional
    public boolean deregisterUserFromActivity(ActivityDeregisterRequestDTO dto) {
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
    public List<ActivityApplicationModel> getAllApplicationsToActivityByAcceptedStatus(
            UUID activityId, boolean accepted) {

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
