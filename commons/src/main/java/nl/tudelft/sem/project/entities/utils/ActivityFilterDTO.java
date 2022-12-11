package nl.tudelft.sem.project.entities.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityFilterDTO {
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
}
