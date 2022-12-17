package nl.tudelft.sem.project.shared;

import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Embeddable
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DateInterval {
    @NotNull
    protected Date startDate;

    @NotNull
    protected Date endDate;
}
