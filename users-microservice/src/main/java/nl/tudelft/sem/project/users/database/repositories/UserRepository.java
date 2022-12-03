package nl.tudelft.sem.project.users.database.repositories;

import nl.tudelft.sem.project.users.database.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Checks if the provided email and username are unique.
     *
     * @param email the email to check
     * @param username the username to check
     * @return whether the email or username already exists in the database
     */
    boolean existsByEmailIgnoreCaseOrUsername(String email, String username);

    /**
     * Checks if the provided username is unique.
     *
     * @param username the username to check
     * @return whether the username already exists in the database
     */
    boolean existsByUsername(String username);
}
