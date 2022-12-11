package nl.tudelft.sem.project;

import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Embeddable
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class DateInterval {
    @NotNull
    protected Date startDate;

    @NotNull
    protected Date endDate;
}
