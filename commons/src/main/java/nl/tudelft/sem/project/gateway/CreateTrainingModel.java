package nl.tudelft.sem.project.gateway;

import lombok.*;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.shared.DateInterval;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTrainingModel {
    @Valid
    @NotNull
    protected String location;
    @Valid
    @NotNull
    protected DateInterval dateInterval;
    @Valid
    @NotNull
    @NotEmpty
    protected List<UUID> boats;
}