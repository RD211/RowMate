package nl.tudelft.sem.template.authentication.domain.providers.implementations;

import java.time.Instant;
import nl.tudelft.sem.template.authentication.domain.providers.TimeProvider;
import org.springframework.stereotype.Component;

/**
 * An abstract time provider to make services testable.
 * The TimeProvider interface can be mocked in order to provide a predetermined current time and
 * make tests independent of the actual current time.
 */
@Component
public class CurrentTimeProvider implements TimeProvider {
    /**
     * Gets current time.
     *
     * @return The current time
     */
    public Instant getCurrentTime() {
        return Instant.now();
    }
}
