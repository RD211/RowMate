package nl.tudelft.sem.project.users.database.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Date;

@Data
@Embeddable
public class DateInterval {
    @Column(nullable = false)
    protected Date startDate;

    @Column(nullable = false)
    protected Date endDate;

    @Column(nullable = false)
    protected boolean recurring;
}
