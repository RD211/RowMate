package nl.tudelft.sem.project.users.domain.certificate;

import nl.tudelft.sem.project.ConverterEntityDTO;
import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.exceptions.CertificateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@Service
public class CertificateConverterService implements ConverterEntityDTO<CertificateDTO, Certificate> {

    @Autowired
    transient CertificateService certificateService;

    @Override
    public CertificateDTO toDTO(Certificate certificate) {
        return CertificateDTO.builder()
                .id(certificate.getId())
                .name(certificate.getName().getValue())
                .supersededId(Optional.ofNullable(certificate.getSuperseded().map(s -> s.getId()).orElse(null)))
                .build();
    }

    @Override
    public Certificate toEntity(CertificateDTO dto) throws CertificateNotFoundException, ConstraintViolationException {
        return Certificate.builder()
                .id(dto.getId())
                .name(new CertificateName(dto.getName()))
                .superseded(dto.getSupersededId().map(id -> certificateService.getCertificateById(id)).orElse(null))
                .build();
    }

    @Override
    public Certificate toDatabaseEntity(CertificateDTO dto) throws CertificateNotFoundException {
        return certificateService.getCertificateById(dto.getId());
    }
}
