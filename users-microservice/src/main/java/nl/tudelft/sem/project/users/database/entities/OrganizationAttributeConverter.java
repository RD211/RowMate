package nl.tudelft.sem.project.users.database.entities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OrganizationAttributeConverter implements AttributeConverter<Organization, String> {

    @Override
    public String convertToDatabaseColumn(Organization attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.toString();
    }

    @Override
    public Organization convertToEntityAttribute(String dbData) {
        return new Organization(dbData);
    }

}
