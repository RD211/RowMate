package nl.tudelft.sem.template.authentication.profiles;

import nl.tudelft.sem.template.authentication.domain.user.PasswordHashingService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This is a configuration profile.
 * A configuration can be used to define beans to be used during injection.
 * When this profile is active spring dependency injection will use this class to look for bean methods.
 * It will then prioritise these beans due to their @Primary tag.
 * In this case we return a mock to allow for more testability.
 * .
 * The profile tag will give a name to this configuration.
 * With the tag applied the profile will be inactive by default unless activated.
 * When the profile is active its bean will be used when looking for Beans to auto-inject.
 *.
 * A configuration profile to allow injection of a mock PasswordEncoder.
 */
@Profile("mockPasswordEncoder")
@Configuration
public class MockPasswordEncoderProfile {

    /**
     * Mocks the PasswordEncoder.
     *
     * @return A mocked PasswordEncoder.
     */
    @Bean
    @Primary  // marks this bean as the first bean to use when trying to inject a PasswordEncoder
    public PasswordEncoder getMockPasswordEncoder() {
        return Mockito.mock(PasswordEncoder.class);
    }

    /**
     * Mocks the Value Object PasswordEncoder.
     *
     * @return A mocked PasswordValueEncoder.
     */
    @Bean
    @Primary  // marks this bean as the first bean to use when trying to inject a PasswordValueEncoder
    public PasswordHashingService getMockPasswordValueEncoder() {
        return Mockito.mock(PasswordHashingService.class);
    }
}
