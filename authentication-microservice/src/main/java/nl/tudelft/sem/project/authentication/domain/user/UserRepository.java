package nl.tudelft.sem.project.authentication.domain.user;

import java.util.Optional;

import nl.tudelft.sem.project.shared.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, String> {

    Optional<AppUser> findByUsername(Username username);

    boolean existsByUsername(Username username);
}
