package nl.tudelft.sem.project.authentication.domain.user;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter for the HashedPassword value object.
 */
@Converter
public class HashedPasswordAttributeConverter implements AttributeConverter<HashedPassword, String> {

    @Override
    public String convertToDatabaseColumn(HashedPassword attribute) {
        return attribute.getHash();
    }

    @Override
    public HashedPassword convertToEntityAttribute(String dbData) {
        return new HashedPassword(dbData);
    }

}

