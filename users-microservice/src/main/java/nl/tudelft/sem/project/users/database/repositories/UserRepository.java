package nl.tudelft.sem.project.users.database.repositories;

import nl.tudelft.sem.project.users.domain.users.User;
import nl.tudelft.sem.project.users.domain.users.UserEmail;
import nl.tudelft.sem.project.shared.Username;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByEmail(UserEmail email);

    Optional<User> findByUsername(Username username);

    boolean existsByUsername(Username username);

    boolean existsByEmail(UserEmail email);
}
