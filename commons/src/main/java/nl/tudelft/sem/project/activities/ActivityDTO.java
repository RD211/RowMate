package nl.tudelft.sem.project.activities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;
import nl.tudelft.sem.project.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
@With
@SuperBuilder
public class ActivityDTO implements DTO {

    protected UUID id;

    @NotNull
    @Size(max = 200, message = "Location has maximum length of 200")
    protected String location;

    @NotNull
    @Size(min = 4, max = 50, message = "Username of owner must be between 4 and 50 characters.")
    protected String owner;

    @NotNull
    protected Date startTime;

    @NotNull
    protected Date endTime;

    @NotNull
    protected List<BoatDTO> boats;
}
