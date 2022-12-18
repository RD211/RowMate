package nl.tudelft.sem.project.entities.utils;

import lombok.*;
import nl.tudelft.sem.project.enums.BoatRole;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityFilterDTO {
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    protected List<BoatRole> preferredRoles;
}
