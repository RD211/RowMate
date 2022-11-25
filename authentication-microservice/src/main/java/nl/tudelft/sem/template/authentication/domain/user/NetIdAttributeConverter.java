package nl.tudelft.sem.template.authentication.domain.user;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Converter for the NetID value object.
 */
@Converter
public class NetIdAttributeConverter implements AttributeConverter<NetId, String> {

    @Override
    public String convertToDatabaseColumn(NetId attribute) {
        return attribute.toString();
    }

    @Override
    public NetId convertToEntityAttribute(String dbData) {
        return new NetId(dbData);
    }

}

