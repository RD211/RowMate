package nl.tudelft.sem.project.matchmaking;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDeregisterRequestDTO {
    protected String userName;
    protected UUID activityId;
}
