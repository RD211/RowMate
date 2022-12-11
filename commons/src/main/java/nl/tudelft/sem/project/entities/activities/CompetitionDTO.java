package nl.tudelft.sem.project.entities.activities;

import lombok.*;
import nl.tudelft.sem.project.entities.DTO;
import nl.tudelft.sem.project.enums.Gender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionDTO extends ActivityDTO {

    @NonNull
    protected Boolean allowsAmateurs;

    @NonNull
    protected String requiredOrganization;

    @NonNull
    protected Gender requiredGender;

    public CompetitionDTO(UUID id, String location, LocalDateTime startTime, LocalDateTime endTime, List<UUID> boats, Boolean allowsAmateurs, String requiredOrganization, Gender requiredGender) {
        this.id = id;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.boats = boats;
        this.allowsAmateurs = allowsAmateurs;
        this.requiredOrganization = requiredOrganization;
        this.requiredGender = requiredGender;
    }
}
