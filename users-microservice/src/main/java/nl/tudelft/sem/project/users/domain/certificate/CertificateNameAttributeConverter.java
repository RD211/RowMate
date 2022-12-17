package nl.tudelft.sem.project.users.domain.certificate;

import javax.persistence.AttributeConverter;

public class CertificateNameAttributeConverter implements AttributeConverter<CertificateName, String> {
    @Override
    public String convertToDatabaseColumn(CertificateName attribute) {
        return attribute.getValue();
    }

    @Override
    public CertificateName convertToEntityAttribute(String dbData) {
        return new CertificateName(dbData);
    }
}
