package nl.tudelft.sem.project.entities.utils;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityFilterDTO {
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
}
