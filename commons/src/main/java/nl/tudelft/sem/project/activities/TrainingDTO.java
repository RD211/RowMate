package nl.tudelft.sem.project.activities;


import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.WithBy;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class TrainingDTO extends ActivityDTO {
    public TrainingDTO(UUID id,
                       String location,
                       String owner,
                       Date startTime,
                       Date endTime,
                       List<BoatDTO> boats) {
        super(id, location, owner, startTime, endTime, boats);
    }
}
