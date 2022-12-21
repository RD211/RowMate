package nl.tudelft.sem.project.matchmaking.services;

import nl.tudelft.sem.project.activities.ActivitiesClient;
import nl.tudelft.sem.project.activities.ActivityDTO;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.BoatsClient;
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
import nl.tudelft.sem.project.notifications.EventType;
import nl.tudelft.sem.project.notifications.NotificationClient;
import nl.tudelft.sem.project.notifications.NotificationDTO;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.UsersClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchmakingService {

    transient ActivitiesClient activitiesClient;
    transient UsersClient usersClient;
    transient BoatsClient boatsClient;

    transient ActivityRegistrationRepository activityRegistrationRepository;

    public static final String autoFindErrorMessage =
        "Unfortunately, we could not find any activity matching your request. Please try again!";

    /**
     * The autowired constructor for the service.
     *
     * @param activitiesClient the activities' client.
     * @param activityRegistrationRepository the activity register repo.
     * @param boatsClient the boats client.
     */
    @Autowired
    public MatchmakingService(
            ActivitiesClient activitiesClient,
            UsersClient usersClient,
            ActivityRegistrationRepository activityRegistrationRepository,
            BoatsClient boatsClient
    ) {
        this.activitiesClient = activitiesClient;
        this.usersClient = usersClient;
        this.activityRegistrationRepository = activityRegistrationRepository;
        this.boatsClient = boatsClient;
    }

    public List<ActivityDTO> findActivities(ActivityRequestDTO dto) {
        return activitiesClient.findActivitiesFromFilter(dto.getActivityFilter());
    }

    /**
     * Automatically matches a user with an activity according to their preferred strategy.
     * At the moment, if strategy is 1, the activity with the earliest start is chosen.
     * Otherwise, the system will pick a random strategy.
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
            determineFeasibility(dto, activity, feasibleActivities);
        }
        return feasibleActivities;
    }

    @SuppressWarnings("PMD")
    private void determineFeasibility(
            ActivityRequestDTO dto,
            ActivityDTO activity,
            List<AvailableActivityModel> feasibleActivities
    ) {


        List<ActivityRegistration> registrations
                = activityRegistrationRepository.findAllByActivityId(activity.getId());
        var boats = activity.getBoats();
        for (int i = 0; i < boats.size(); i++) {

            BoatDTO boat = boatsClient.getBoat(boats.get(i).getBoatId());

            checkBoatAvailability(dto, activity, feasibleActivities, registrations, boat, i);
        }
    }

    @SuppressWarnings("PMD")
    private void checkBoatAvailability(
            ActivityRequestDTO dto,
            ActivityDTO activity,
            final List<AvailableActivityModel> feasibleActivities,
            final List<ActivityRegistration> registrations,
            BoatDTO boat,
            final int idx
    ) {

        List<ActivityRegistration> forThisBoat = getRegistrationsForBoat(registrations, idx);
        for (BoatRole role : dto.getActivityFilter().getPreferredRoles()) {
            List<ActivityRegistration> registrationsForRole = getRegistrationsForRole(forThisBoat, role);
            List<BoatRole> availableSpotsForRole = getAvailableSpotForRole(boat, role);

            if (availableSpotsForRole.size() > registrationsForRole.size()) {
                feasibleActivities.add(
                        AvailableActivityModel
                                .builder()
                                .activityDTO(activity)
                                .boat(idx)
                                .role(role)
                                .build()
                );
                break;
            }
        }
    }

    private List<ActivityRegistration> getRegistrationsForRole(List<ActivityRegistration> registrations, BoatRole role) {
        return registrations.stream()
                .filter(b -> b.getRole() == role)
            .collect(Collectors.toList());
    }


    private List<BoatRole> getAvailableSpotForRole(BoatDTO boat, BoatRole role) {
        return boat.getAvailablePositions()
            .stream().filter(b -> b == role).collect(Collectors.toList());
    }

    private List<ActivityRegistration> getRegistrationsForBoat(List<ActivityRegistration> registrations, int idx) {
        return registrations.stream()
            .filter(b -> b.getBoat() == idx)
            .collect(Collectors.toList());
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

        List<ActivityRegistration> overlappingRegistrations
            = activityRegistrationRepository.findRequestOverlap(
                    dto.getActivityId(),
                    dto.getUserName(),
                    dto.getBoat(),
                    dto.getBoatRole()
            );

        if (!overlappingRegistrations.isEmpty()) {
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
}
