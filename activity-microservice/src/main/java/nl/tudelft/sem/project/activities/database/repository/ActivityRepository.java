package nl.tudelft.sem.project.activities.database.repository;

import lombok.NonNull;
import nl.tudelft.sem.project.activities.database.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    String timeSlotQuery = "SELECT * FROM activities"
            + " WHERE start_time >= :activityStartTime and end_time <= :activityEndTime";

    /**
     * Finds the activities that are placed within the specified time slot.
     *
     * @param activityStartTime the minimum start time of the activity
     * @param activityEndTime the maximum end time of the activity
     * @return a list of the activities that will happen inside the time interval
     */
    @Query(value = timeSlotQuery, nativeQuery = true)
    List<Activity> findWithinTimeSlot(LocalDateTime activityStartTime, LocalDateTime activityEndTime);

    @Override
    @NonNull
    Optional<Activity> findById(@NonNull UUID uuid);
}
