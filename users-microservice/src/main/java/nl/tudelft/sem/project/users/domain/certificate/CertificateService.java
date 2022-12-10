package nl.tudelft.sem.project.users.domain.certificate;

import nl.tudelft.sem.project.users.database.repositories.CertificateRepository;
import org.springframework.stereotype.Service;

/**
 * A DDD service for managing certificate.
 */
@Service
public class CertificateService {
    private final transient CertificateRepository certificateRepository;

    public CertificateService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    /**
     * Register a new user.
     *
     * @param netId    The NetID of the user
     * @param password The password of the user
     * @throws Exception if the user already exists
     */
    public AppUser registerUser(NetId netId, Password password) throws Exception {

        if (checkNetIdIsUnique(netId)) {
            // Hash password
            HashedPassword hashedPassword = passwordHashingService.hash(password);

            // Create new account
            AppUser user = new AppUser(netId, hashedPassword);
            userRepository.save(user);

            return user;
        }

        throw new NetIdAlreadyInUseException(netId);
    }

    public boolean checkNetIdIsUnique(NetId netId) {
        return !userRepository.existsByNetId(netId);
    }
}
