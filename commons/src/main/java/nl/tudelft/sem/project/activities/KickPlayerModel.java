package nl.tudelft.sem.project.activities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KickPlayerModel {
    @Valid
    @NotNull
    protected UUID activityId;
    @Valid
    @NotNull
    protected UUID userId;
}
