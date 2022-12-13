package nl.tudelft.sem.project.users.domain.certificate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@ToString
@EqualsAndHashCode
@Validated
public class CertificateName {
    @NotNull
    @Size(min = 4, max = 50, message = "Name must be between 4 and 50 characters")
    private final String value;

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
