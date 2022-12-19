package nl.tudelft.sem.project.matchmaking.domain;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class ActivityRegistrationId implements Serializable {
    private String userName;
    private UUID activityId;


    public static final long serialVersionUID = 1;
}
