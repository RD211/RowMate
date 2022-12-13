package nl.tudelft.sem.project.shared;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@EqualsAndHashCode
@Validated
@Setter
@NoArgsConstructor
public class Username {

    @NotNull
    @Size(min = 4, max = 50, message = "Name must be between 4 and 50 characters")
    protected String name;

    /**
     * The username constructor.
     * Uses hibernate validator to automatically validate the annotations.
     *
     * @param name the raw string email.
     */
    public Username(@NonNull String name) {
        this.name = name;

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
        return getName();
    }
}
