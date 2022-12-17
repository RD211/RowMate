package nl.tudelft.sem.project.users;

import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.springframework.validation.annotation.Validated;

import javax.validation.*;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@EqualsAndHashCode
@Validated
@NoArgsConstructor
public class UserEmail {
    @Email(message = "Email must be valid.")
    @NotNull
    private String email;

    /**
     * The user email constructor.
     * Uses hibernate validator to automatically validate the annotations.
     *
     * @param email the raw string email.
     */
    public UserEmail(@NonNull String email) {
        this.email = email;

        // This validates every field of the object.
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator;
        try {
            validator = factory.getValidator();
        } finally {
            factory.close();
        }

        var results = validator.validate(this);
        if (!results.isEmpty()) {
            throw new ConstraintViolationException(results);
        }
    }
}