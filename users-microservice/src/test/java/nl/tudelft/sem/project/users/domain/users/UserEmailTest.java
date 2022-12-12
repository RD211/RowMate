package nl.tudelft.sem.project.users.domain.users;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

class UserEmailTest {

    @Test
    public void testConstructorValid() {
        var email = "test@gmail.com";
        assertDoesNotThrow(() -> new UserEmail(email));
        assertEquals(email, new UserEmail(email).getEmail());
    }

    @Test
    public void testConstructorInvalid() {
        assertThrows(NullPointerException.class, () -> new UserEmail(null));
    }

    @Test
    public void testConstructorValidation() {
        assertThrows(ConstraintViolationException.class, () -> new UserEmail("ad"));
    }
}