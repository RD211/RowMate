package nl.tudelft.sem.template.authentication.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

/**
 * A base class for adding domain event support to an entity.
 */
public abstract class HasEvents {
    private final transient List<Object> domainEvents = new ArrayList<>();

    protected void recordThat(Object event) {
        domainEvents.add(Objects.requireNonNull(event));
    }

    @DomainEvents
    protected Collection<Object> releaseEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    @AfterDomainEventPublication
    protected void clearEvents() {
        this.domainEvents.clear();
    }
}
