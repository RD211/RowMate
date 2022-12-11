package nl.tudelft.sem.project.users.domain.users;

import lombok.NonNull;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {
    @NonNull Optional<User> findById(@NonNull UUID id);

    Optional<User> findByEmail(UserEmail email);

    Optional<User> findByUsername(Username username);

    boolean existsByUsername(Username username);

    boolean existsByEmail(UserEmail email);
}
