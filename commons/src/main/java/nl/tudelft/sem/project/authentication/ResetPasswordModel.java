package nl.tudelft.sem.project.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordModel {
    @Valid @NotNull
    private AppUserModel appUserModel;
    @Valid @NotNull
    private Password newPassword;
}