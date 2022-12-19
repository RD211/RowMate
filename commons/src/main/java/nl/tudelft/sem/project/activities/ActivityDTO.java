package nl.tudelft.sem.project.activities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;
import nl.tudelft.sem.project.DTO;
import nl.tudelft.sem.project.utils.Existing;
import nl.tudelft.sem.project.utils.Fictional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TrainingDTO.class, name = "TrainingDTO"),

        @JsonSubTypes.Type(value = CompetitionDTO.class, name = "CompetitionDTO") })
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ActivityDTO implements DTO {

    @NotNull(groups={Existing.class})
    protected UUID id;

    @NotNull(groups={Fictional.class})
    protected String location;

    @NotNull(groups={Fictional.class})
    protected String owner;

    @NotNull(groups={Fictional.class})
    protected Date startTime;

    @NotNull(groups={Fictional.class})
    protected Date endTime;

    @NotNull(groups={Fictional.class})
    protected List<BoatDTO> boats;
}
