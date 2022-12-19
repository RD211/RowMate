package nl.tudelft.sem.project.authentication.domain.user;

import java.util.Optional;

import nl.tudelft.sem.project.shared.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Username> {

    Optional<AppUser> findByUsername(Username username);

    boolean existsByUsername(Username username);

    @Transactional
    @Modifying
    @Query("update AppUser a set a.password = ?1 where a.username = ?2")
    void updatePasswordByUsername(HashedPassword password, Username username);
}
