package nl.tudelft.sem.project.matchmaking.domain;


import lombok.*;
import nl.tudelft.sem.project.enums.BoatRole;

import javax.persistence.*;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Entity
@Table(name = "activity_registrations")
@IdClass(ActivityRegistrationId.class)
public class ActivityRegistration {
    @Id
    @Column(name = "user_name", nullable = false)
    protected String userName;

    @Id
    @Column(name = "activity_id", nullable = false)
    protected UUID activityId;


    protected int boat;

    @Enumerated(EnumType.STRING)
    protected BoatRole role;
}
