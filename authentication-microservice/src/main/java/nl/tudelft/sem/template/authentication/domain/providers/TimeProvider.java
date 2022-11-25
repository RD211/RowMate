package nl.tudelft.sem.template.authentication.domain.providers;

import java.time.Instant;

/**
 * An abstract time provider to make services testable.
 * This interface can be mocked in order to provide a predetermined current time and
 * make tests independent of the actual current time.
 */
public interface TimeProvider {
    /**
     * Retrieves the current time.
     *
     * @return The current time
     */
    Instant getCurrentTime();
}
