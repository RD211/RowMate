package nl.tudelft.sem.project.users.database.repositories;

import nl.tudelft.sem.project.users.domain.certificate.Certificate;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CertificateRepository extends PagingAndSortingRepository<Certificate, UUID> {
    Optional<Certificate> findByName(String name);
}

