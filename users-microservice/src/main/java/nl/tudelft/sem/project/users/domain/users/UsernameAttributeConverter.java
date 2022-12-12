package nl.tudelft.sem.project.users.domain.users;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UsernameAttributeConverter implements AttributeConverter<Username, String> {

    @Override
    public String convertToDatabaseColumn(Username attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getName();
    }

    @Override
    public Username convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return new Username(dbData);
    }

}