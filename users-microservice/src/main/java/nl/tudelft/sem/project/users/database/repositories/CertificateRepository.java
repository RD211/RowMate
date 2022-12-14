package nl.tudelft.sem.project.users.database.repositories;

import nl.tudelft.sem.project.users.domain.certificate.Certificate;
import nl.tudelft.sem.project.users.domain.certificate.CertificateName;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CertificateRepository extends CrudRepository<Certificate, UUID> {
    Optional<Certificate> findByName(CertificateName name);

    boolean existsByName(CertificateName name);
}

