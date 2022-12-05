package nl.tudelft.sem.project.users.database.repositories;

import nl.tudelft.sem.project.users.database.entities.User;
import nl.tudelft.sem.project.users.database.entities.UserEmail;
import nl.tudelft.sem.project.users.database.entities.Username;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {
    Optional<User> findByEmail(UserEmail email);

    Optional<User> findByUsername(Username username);

    boolean existsByEmailOrUsername(UserEmail email, Username username);

    boolean existsByUsername(Username username);

}
