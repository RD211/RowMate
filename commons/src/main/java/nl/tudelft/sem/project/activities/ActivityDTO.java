package nl.tudelft.sem.project.activities;

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