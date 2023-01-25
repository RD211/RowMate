package nl.tudelft.sem.project.matchmaking.strategies;


import nl.tudelft.sem.project.matchmaking.models.AvailableActivityModel;
import nl.tudelft.sem.project.matchmaking.models.FoundActivityModel;

import java.util.Random;

public class RandomStrategy extends MatchingStrategy {
    /**
     * Returns a FoundActivityModel with a random activity.
     *
     * @return a FoundActivityModel created from a random activity,
     *      null if there are no available activities.
     */
    public FoundActivityModel findActivityToRegister() {
        if (requestData == null) {
            return null;
        }
        if (getAvailableActivities().size() == 0) {
            return null;
        }

        Random rand = new Random();
        AvailableActivityModel activityModel
            = getAvailableActivities().get(rand.nextInt(getAvailableActivities().size()));

        return createResultFromPick(activityModel);
    }
}
