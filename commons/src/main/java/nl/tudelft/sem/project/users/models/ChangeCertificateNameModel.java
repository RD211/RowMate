package nl.tudelft.sem.project.users.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.project.users.CertificateDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeCertificateNameModel {
    @Valid @NotNull
    private CertificateDTO certificateDTO;
    @Valid @NotNull
    private String newName;
}