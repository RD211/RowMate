package nl.tudelft.sem.project.shared;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OrganizationAttributeConverter implements AttributeConverter<Organization, String> {

    @Override
    public String convertToDatabaseColumn(Organization attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getName();
    }

    @Override
    public Organization convertToEntityAttribute(String dbData) {
        return new Organization(dbData);
    }

}
