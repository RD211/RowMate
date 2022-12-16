package nl.tudelft.sem.project.entities.activities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import nl.tudelft.sem.project.entities.DTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ActivityDTO implements DTO {

    protected UUID id;

    @NonNull
    protected String location;

    @NonNull
    protected String owner;

    @NonNull
    protected LocalDateTime startTime;

    @NonNull
    protected LocalDateTime endTime;

    @NonNull
    protected List<UUID> boats;
}