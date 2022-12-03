package nl.tudelft.sem.project.users.database.repositories;

import nl.tudelft.sem.project.users.database.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmailIgnoreCaseOrUsername(String email, String username);

    boolean existsByUsername(String username);

}
