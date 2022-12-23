package nl.tudelft.sem.project.gateway;

import lombok.*;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.shared.DateInterval;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCompetitionModel {
    @Valid
    @NotNull
    @Size(max = 200, message = "Location has maximum length of 200")
    protected String location;
    @Valid
    @NotNull
    protected DateInterval dateInterval;
    @Valid
    @NotNull
    @NotEmpty
    protected List<UUID> boats;
    @Valid @NotNull
    protected Boolean allowsAmateurs;
    protected String requiredOrganization;
    protected Gender requiredGender;
}