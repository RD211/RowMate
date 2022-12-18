package nl.tudelft.sem.project.users;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UserEmailAttributeConverter implements AttributeConverter<UserEmail, String> {

    @Override
    public String convertToDatabaseColumn(UserEmail attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getEmail();
    }

    @Override
    public UserEmail convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return new UserEmail(dbData);
    }

}
