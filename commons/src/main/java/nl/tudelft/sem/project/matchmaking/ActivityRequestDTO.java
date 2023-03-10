package nl.tudelft.sem.project.matchmaking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequestDTO {
    protected String userName;
    protected ActivityFilterDTO activityFilter;
}
