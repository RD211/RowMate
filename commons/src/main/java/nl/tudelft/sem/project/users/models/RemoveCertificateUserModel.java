package nl.tudelft.sem.project.users.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.utils.Existing;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Validated({Existing.class})
@AllArgsConstructor
@NoArgsConstructor
public class RemoveCertificateUserModel {
    @Valid @NotNull
    protected UserDTO user;
    @Valid @NotNull
    protected CertificateDTO certificate;
}