package nl.tudelft.sem.project.users.database.entities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UserEmailAttributeConverter implements AttributeConverter<UserEmail, String> {

    @Override
    public String convertToDatabaseColumn(UserEmail attribute) {
        return attribute.toString();
    }

    @Override
    public UserEmail convertToEntityAttribute(String dbData) {
        return new UserEmail(dbData);
    }

}
