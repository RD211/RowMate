package nl.tudelft.sem.project.matchmaking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRegistrationResponseDTO {

    @NotNull
    protected String userName;

    @NotNull
    protected UUID activityId;

    @NotNull
    protected boolean accepted;
}
