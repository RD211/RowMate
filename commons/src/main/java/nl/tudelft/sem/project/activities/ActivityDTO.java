package nl.tudelft.sem.project.activities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TrainingDTO.class, name = "TrainingDTO"),

        @JsonSubTypes.Type(value = CompetitionDTO.class, name = "CompetitionDTO") })
public abstract class ActivityDTO {

}
