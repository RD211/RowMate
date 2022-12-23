package nl.tudelft.sem.project.users.domain.certificate;

import nl.tudelft.sem.project.users.CertificateDTO;
import javax.validation.ConstraintViolationException;

import nl.tudelft.sem.project.users.CertificateName;
import nl.tudelft.sem.project.users.exceptions.CertificateNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CertificateConverterServiceTest {

    @Mock
    private CertificateService certificateService;

    @InjectMocks
    private CertificateConverterService sut;

    @Test
    void toDTO() {
        var cert = new Certificate("A name", new Certificate("BLAH"));
        var dto = sut.toDTO(cert);

        assertThat(dto.getId()).isEqualTo(cert.getId());
        assertThat(dto.getName()).isEqualTo(cert.getName().getValue());
        assertThat(dto.getSupersededId()).isEqualTo(cert.getSuperseded().get().getId());
    }

    @Test
    void toEntityWithBadName() {
        var dto = CertificateDTO.builder()
                .id(UUID.randomUUID())
                .name("A")
                .supersededId(null)
                .build();
        assertThatThrownBy(() -> sut.toEntity(dto)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void toEntityWithNonExistentSuperseding() {
        var id = UUID.randomUUID();
        var dto = CertificateDTO.builder()
                .id(UUID.randomUUID())
                .name("A valid name")
                .supersededId(id)
                .build();
        when(certificateService.getCertificateById(id)).thenThrow(CertificateNotFoundException.class);
        assertThatThrownBy(() -> sut.toEntity(dto)).isInstanceOf(CertificateNotFoundException.class);
    }

    @Test
    void toEntityWorkingWithEmptySuperseding() {
        var dto = CertificateDTO.builder()
                .id(UUID.randomUUID())
                .name("A good name")
                .supersededId(null)
                .build();
        var entity = sut.toEntity(dto);
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getName().getValue()).isEqualTo(dto.getName());
        assertThat(entity.getSuperseded().map(c -> c.getId())).isEqualTo(Optional.ofNullable(dto.getSupersededId()));
    }

    @Test
    void toEntityWorkingWithPresentSuperseding() {
        var id = UUID.randomUUID();
        var dto = CertificateDTO.builder()
                .id(UUID.randomUUID())
                .name("A good name")
                .supersededId(id)
                .build();
        var superseded = new Certificate(id, new CertificateName("Some cert"), null);

        when(certificateService.getCertificateById(id))
                .thenReturn(superseded);
        var entity = sut.toEntity(dto);


        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getName().getValue()).isEqualTo(dto.getName());
        assertThat(entity.getSuperseded().map(c -> c.getId())).isEqualTo(Optional.of(dto.getSupersededId()));
    }

    @Test
    void toDatabaseEntity() {
        var id = UUID.randomUUID();
        var dbEntity = new Certificate(id, new CertificateName("A valid name"), null);
        var dto = CertificateDTO.builder().id(id).build();
        when(certificateService.getCertificateById(id)).thenReturn(dbEntity);
        assertThat(sut.toDatabaseEntity(dto)).isEqualTo(dbEntity);
    }
}