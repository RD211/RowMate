package nl.tudelft.sem.template.authentication.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import nl.tudelft.sem.template.authentication.domain.user.AppUser;
import nl.tudelft.sem.template.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.template.authentication.domain.user.NetId;
import nl.tudelft.sem.template.authentication.domain.user.UserRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockPasswordEncoder"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JwtUserDetailsServiceTests {

    @Autowired
    private transient JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private transient UserRepository userRepository;

    @Test
    public void loadUserByUsername_withValidUser_returnsCorrectUser() {
        // Arrange
        final NetId testUser = new NetId("SomeUser");
        final HashedPassword testHashedPassword = new HashedPassword("password123Hash");

        AppUser appUser = new AppUser(testUser, testHashedPassword);
        userRepository.save(appUser);

        // Act
        UserDetails actual = jwtUserDetailsService.loadUserByUsername(testUser.toString());

        // Assert
        assertThat(actual.getUsername()).isEqualTo(testUser.toString());
        assertThat(actual.getPassword()).isEqualTo(testHashedPassword.toString());
    }

    @Test
    public void loadUserByUsername_withNonexistentUser_throwsException() {
        // Arrange
        final String testNonexistentUser = "SomeUser";

        final NetId testUser = new NetId("AnotherUser");
        final String testPasswordHash = "password123Hash";

        AppUser appUser = new AppUser(testUser, new HashedPassword(testPasswordHash));
        userRepository.save(appUser);

        // Act
        ThrowableAssert.ThrowingCallable action = () -> jwtUserDetailsService.loadUserByUsername(testNonexistentUser);

        // Assert
        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(action);
    }
}
