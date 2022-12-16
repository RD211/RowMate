package nl.tudelft.sem.project.authentication.authentication;

import java.util.ArrayList;
import nl.tudelft.sem.project.authentication.domain.user.UserRepository;
import nl.tudelft.sem.project.shared.Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * User details service responsible for retrieving the user from the DB.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final transient UserRepository userRepository;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user information required for authentication from the DB.
     *
     * @param username The username of the user we want to authenticate
     * @return The authentication user information of that user
     * @throws UsernameNotFoundException Username was not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var optionalUser = userRepository.findByUsername(new Username(username));

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist");
        }

        var user = optionalUser.get();

        return new User(user.getUsername().getName(), user.getPassword().getHash(),
                new ArrayList<>(){}); // no authorities/roles
    }
}
