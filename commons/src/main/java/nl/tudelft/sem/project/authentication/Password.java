package nl.tudelft.sem.project.authentication;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DDD value object representing a password in our domain.
 */
@EqualsAndHashCode
@Data
@NoArgsConstructor
public class Password {
    @NotNull
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    protected String passwordValue;

    public Password(@NonNull String password) {
        this.passwordValue = password;

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

    @Override
    public String toString() {
        return getPasswordValue();
    }
}
