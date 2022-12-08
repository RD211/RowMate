package nl.tudelft.sem.project.authentication.domain.user;

/**
 * A DDD domain event that indicated a user was created.
 */
public class UserWasCreatedEvent {
    private final NetId netId;

    public UserWasCreatedEvent(NetId netId) {
        this.netId = netId;
    }

    public NetId getNetId() {
        return this.netId;
    }
}
