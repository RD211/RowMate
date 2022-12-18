package nl.tudelft.sem.project.authentication.config;

import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.authentication.domain.user.RegistrationService;
import nl.tudelft.sem.project.shared.Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * This will run when the boot run starts.
 * It will check if the admin is already registered and if not it will
 * add an admin record to the users table.
 */
@Component
public class AdminAdder {

    @Autowired
    transient RegistrationService registrationService;

    /**
     * Adds the admin user at startup.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        try {
            registrationService.registerUser(
                    new Username("administrator"),
                    new Password("administrator"),
                    true
            );
        } catch (Exception e) {
            System.out.println("Admin already registered.");
        }
    }
}