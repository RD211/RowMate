package nl.tudelft.sem.project.matchmaking;

import lombok.*;
import nl.tudelft.sem.project.enums.BoatRole;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityFilterDTO {
    @Valid @NotNull
    protected Date startTime;
    protected Date endTime;

    protected List<BoatRole> preferredRoles;
}
