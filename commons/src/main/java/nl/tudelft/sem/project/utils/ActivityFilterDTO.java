package nl.tudelft.sem.project.utils;

import lombok.*;
import nl.tudelft.sem.project.enums.BoatRole;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityFilterDTO {
    protected Date startTime;
    protected Date endTime;

    protected List<BoatRole> preferredRoles;
}
