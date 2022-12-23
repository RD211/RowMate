package nl.tudelft.sem.project.activities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChangeActivityTimeModel {
    protected UUID activityId;
    protected Date newStartDate;
    protected Date newEndDate;
}
