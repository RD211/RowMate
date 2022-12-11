package nl.tudelft.sem.project.entities.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequestDTO {

    protected UUID userId;
    protected ActivityFilterDTO activityFilter;
}
