package nl.tudelft.sem.project.activities;

import lombok.*;
import nl.tudelft.sem.project.enums.Gender;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Validated
public class CompetitionDTO extends ActivityDTO {

    @NotNull @Valid
    protected Boolean allowsAmateurs;

    protected String requiredOrganization;
    protected Gender requiredGender;

    public CompetitionDTO(UUID id, String location, String owner, Date startTime, Date endTime, List<BoatDTO> boats, Boolean allowsAmateurs, String requiredOrganization, Gender requiredGender) {
        super(id, location, owner, startTime, endTime, boats);
        this.allowsAmateurs = allowsAmateurs;
        this.requiredOrganization = requiredOrganization;
        this.requiredGender = requiredGender;
    }
}
