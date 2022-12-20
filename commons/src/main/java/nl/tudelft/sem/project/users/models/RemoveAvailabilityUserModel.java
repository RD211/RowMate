package nl.tudelft.sem.project.users.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.project.shared.DateInterval;
import nl.tudelft.sem.project.users.UserDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveAvailabilityUserModel {
    @Valid
    @NotNull
    protected UserDTO user;
    @Valid @NotNull
    protected DateInterval dateInterval;
}