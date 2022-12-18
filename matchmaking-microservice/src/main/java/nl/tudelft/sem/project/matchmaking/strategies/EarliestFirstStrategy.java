package nl.tudelft.sem.project.matchmaking.strategies;

import nl.tudelft.sem.project.matchmaking.models.AvailableActivityModel;
import nl.tudelft.sem.project.matchmaking.models.FoundActivityModel;

import java.time.LocalDateTime;
import java.util.List;

public class EarliestFirstStrategy extends MatchingStrategy {
    /**
     * Finds the earliest activity and returns a FoundActivityModel with it.
     *
     * @return a FoundModelActivity with the earliest activity in it,
     *      null if no activities are available.
     */
    public FoundActivityModel findActivityToRegister() {
        List<AvailableActivityModel> activities = getAvailableActivities();
        if (activities.size() == 0) {
            return null;
        }

        AvailableActivityModel earliest = activities.get(0);

        for (AvailableActivityModel activity : activities) {
            LocalDateTime crtTime = earliest.getActivityDTO().getStartTime();
            LocalDateTime candidateTime = activity.getActivityDTO().getStartTime();
            if (candidateTime.isBefore(crtTime)) {
                earliest = activity;
            }
        }

        return createResultFromPick(earliest);
    }
}
