package nl.tudelft.sem.project.activities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nl.tudelft.sem.project.DTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TrainingDTO.class, name = "TrainingDTO"),

        @JsonSubTypes.Type(value = CompetitionDTO.class, name = "CompetitionDTO") })
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
