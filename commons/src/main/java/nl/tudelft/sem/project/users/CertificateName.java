package nl.tudelft.sem.project.users;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Embeddable;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@ToString
@EqualsAndHashCode
@Validated
public class CertificateName implements Serializable {
    @NotNull
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private final String value;

    /**
     * Constructor for a certificate name. Check if the name String satisfies the constraints.
     *
     * @param name A string from which the name should be created.
     * @throws ConstraintViolationException Is thrown when the parameter does not satisfy the constraints.
     */
    public CertificateName(String name) throws ConstraintViolationException {
        this.value = name;

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
