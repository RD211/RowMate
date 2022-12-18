package nl.tudelft.sem.project.matchmaking.domain;

import nl.tudelft.sem.project.enums.BoatRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ActivityRegistrationRepository extends JpaRepository<ActivityRegistration, ActivityRegistrationId> {
    String overlapQuery = "SELECT * FROM activity_registrations"
            + " WHERE activity_id = :activityId AND "
            + " (user_name = :userName OR (boat = :boat AND role = :#{#role?.toString()}))";

    String activityIdQuery = "SELECT * FROM activity_registrations"
            + " WHERE activity_id = :activityId";

    /**
     * Finds any activity registration that overlaps with a request.
     * e.g., if a user is already registered to the same activity/someone else is registered on the same boat and role
     *
     * @return the activity registration overlapping with the request.
     **/
    @Query(value = overlapQuery, nativeQuery = true)
    List<ActivityRegistration> findRequestOverlap(
            @Param("activityId") UUID activityId,
            @Param("userName") String userName,
            @Param("boat") int boat,
            @Param("role") BoatRole role
    );

    /**
     * Finds all the activity registration by a UUID.
     *
     * @param activityId the UUID to search for.
     * @return the list of all activity registrations associated with the activity
     */
    @Query(value = activityIdQuery, nativeQuery = true)
    List<ActivityRegistration> findAllByActivityId(
            @Param("activityId") UUID activityId
    );
}
