package nl.tudelft.sem.project.users;

import lombok.*;
import nl.tudelft.sem.project.DTO;
import nl.tudelft.sem.project.DateInterval;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.shared.Organization;
import nl.tudelft.sem.project.utils.Existing;
import nl.tudelft.sem.project.utils.Fictional;
import org.hibernate.validator.constraints.Email;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements DTO {
    /**
     * The user's ID.
     */
    @NotNull(groups = {Existing.class})
    protected UUID id;

    /**
     * User's name.
     */
    @NotNull(groups = {Fictional.class})
    @Size(min = 4, max = 50, message = "Name must be between 4 and 50 characters.", groups={Fictional.class})
    protected String username;

    /**
     * User's email address.
     */
    @NotNull(groups = {Fictional.class})
    @Email(groups = {Fictional.class}, message = "Email must be valid.")
    protected String email;

    /**
     * User's organization.
     */
    protected Organization organization;

    /**
     * User's gender.
     */
    protected Gender gender;

    /**
     * If the user is an amateur or not.
     */
    @Builder.Default
    protected boolean isAmateur = true;

    /**
     * User's boat roles.
     */
    @Builder.Default
    protected Set<BoatRole> boatRoles = new HashSet<>();

    /**
     * User's available date intervals.
     */
    @Builder.Default
    protected Set<DateInterval> availableTime = new HashSet<>();

    /**
     * User's owned certificates.
     */
    @Builder.Default
    protected Set<CertificateDTO> certificates = new HashSet<>();
}
