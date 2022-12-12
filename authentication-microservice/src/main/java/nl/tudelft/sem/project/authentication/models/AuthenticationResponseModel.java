package nl.tudelft.sem.project.authentication.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing an authentication response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseModel {
    private String token;
}