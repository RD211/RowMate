package nl.tudelft.sem.template.authentication.domain.user;

/**
 * A DDD domain event indicating a password had changed.
 */
public class PasswordWasChangedEvent {
    private final AppUser user;

    public PasswordWasChangedEvent(AppUser user) {
        this.user = user;
    }

    public AppUser getUser() {
        return this.user;
    }
}
